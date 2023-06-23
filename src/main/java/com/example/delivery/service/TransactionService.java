package com.example.delivery.service;

import com.example.delivery.entity.*;
import com.example.delivery.payload.ForDeliveryRegions;
import com.example.delivery.payload.Response;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.payload.TransactionDto;
import com.example.delivery.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    OfferRepository offerRepository;
    @Autowired
    CarrierRepository carrierRepository;

    /**
     * bu method lokatsiya va yetkazib beruvchini tekshirib beradi
     * Tashuvchi (carrier) ham etkazib berish, ham olib ketish joylariga
     * (ya'ni, tegishli hududlarga)xizmat qilishi kerak
     *
     * @return maker ResponseMessage
     */
    private ResponseMessage maker(TransactionDto dto) {

        Optional<Carrier> optionalCarrier = carrierRepository.findById(dto.getCarrierId());
        if (optionalCarrier.isEmpty())
            return new ResponseMessage(dto.getCarrierId() + " Bunday tashuvchi topilmadi", false);

        Optional<Request> optionalRequest = requestRepository.findById(dto.getRequestId());
        if (optionalRequest.isEmpty())
            return new ResponseMessage(dto.getRequestId() + " bunday so'rov topilmadi topilmadi", false);

        Optional<Offer> optionalOffer = offerRepository.findById(dto.getOfferId());
        if (optionalOffer.isEmpty())
            return new ResponseMessage(dto.getOfferId() + " bunday taklif topilmadi", false);


        // i. talabi uchun
        Request request = optionalRequest.get();
        if (transactionRepository.existsByRequest(request))
            return new ResponseMessage("bu so'rov boshqa transaksiayda bajarilgan", false);

        Offer offer = optionalOffer.get();
        if (transactionRepository.existsByOffer(offer)) {
            return new ResponseMessage("bu taklif boshqa transaksiyada bajarilgan", false);
        }

        // ii. talabi uchun
        List<Product> requestProducts = request.getProducts();
        List<Product> offerProducts = offer.getProducts();

        if (offerProducts.size() != requestProducts.size())
            return new ResponseMessage("taklif va so'rov mahsulotlari bir biriga to'gri kelmagan", false);

        for (Product requestProduct : requestProducts) {
            boolean equals = false;
            for (Product offerProduct : offerProducts) {
                if (requestProduct.equals(offerProduct)) {
                    equals = true;
                }
            }
            if (!equals) {
                return new ResponseMessage("taklif va so'rov mahsulotlari bir biriga to'gri kelmagan", false);
            }
        }


        // iii. talab uchun
        Carrier carrier = optionalCarrier.get();
        boolean b = false;
        boolean d = false;
        for (Location location : carrier.getLocations()) {
            if (location.equals(request.getLocation()))
                b = true;
            if (location.equals(offer.getLocation()))
                d = true;
        }
        if (!b || !d)
            return new ResponseMessage("Yetkazib berish hududi emas", false);


        Transaction transaction = new Transaction(carrier, offer, request);
        return new ResponseMessage("", true, transaction);
    }


    /**
     * Tizimga minimumScore (int minimumScore) qiymat jo’natish orqali tashuvchilar (carriers)
     * va bir xil tashuvchiga (carrier ga) tegishli tranzaksiyalarning umumiy bali qaytariladi.
     * bali berilgan minimal balldan (minimumScore) past boʻlgan tranzaksiya eʼtiborga
     * olinmaydi. Tashuvchilar (carriers) alifbo tartibida saralangan holda qaytariladi.
     */
    public Map<Carrier, Integer> scorePerCarrier(Integer minimumScore) {
        Map<Carrier, Integer> map = new TreeMap<>();

        int score = 0;
        List<Transaction> withMinScore = transactionRepository.getScoreAndCarrier(minimumScore);
        for (Transaction transaction : withMinScore) {
            List<Transaction> sameCarrier = transactionRepository.findByCarrier(transaction.getCarrier());
            for (Transaction singleTran : sameCarrier) {
                score = score + singleTran.getScore();
            }
            map.put(transaction.getCarrier(), score);
            score = 0;
        }

        return map;
    }


    /**
     * har bir masulot uchun (productId) uchun tranzaktsiyalar sonini (faqat 0 dan katta bo'lsa)
     * qaytaradi, masulot identificatori bo’yicha saralangan holatda qaytishi kerak.
     */
    public ForReturn perProduct() {
        Map<String, Integer> map = new TreeMap<>();
        List<Response> responses = new ArrayList<>();

        for (Request request : transactionRepository.getRequestFromTran()) {

            boolean equalProducts = false;
            for (Product product : requestRepository.getProducts(request.getRequestId())) {

                if (map.get(product.getId()) == null) {
                    map.put(product.getId(), 1);
                    equalProducts = true;
                } else if (!equalProducts) {
                    Integer count = map.get(product.getId());
                    map.put(product.getId(), count + 1);
                }
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Response response = new Response(entry.getKey(), entry.getValue());
            responses.add(response);
        }
        ForReturn forReturn = new ForReturn();
        forReturn.setResponse(responses);
        return forReturn;
    }


    /**
     * api methodida bir xil miqdordagi tranzaktsiyalar uchun etkazib berish joylari bo'lgan
     * hududlar nomlari ro'yxatini (alifbo tartibida tartiblangan) qaytaradi. Ya’ni har bir
     * huduga tegishli transaksiya sonlari (transactionNumber bo’lsin) topiladi va shu
     * transaction number va qaysi hududlarni tranasaksiyalar soni shu transaction numberga
     * tengligi qaytariladi.Hudud tranzaktsiyaga tegishli so'rovni yetkazib berish joyini o'z
     * ichiga olgan bo'lsa, tranzaktsiyani yetkazib berish joyidir. Qaytarilgan response da
     * tranzaktsiyalar soni kamayish tartibida bo’lishi kerak.
     */
    public ForReturn deliveryRegionsPerNT() {
        List<ForDeliveryRegions> list = new ArrayList<>();
        List<Location> locations = transactionRepository.getLocationsFromReqFromTran();
        Set<Location> set = new HashSet<>(locations);
        for (Location location : set) {
            ForDeliveryRegions f = new ForDeliveryRegions();

            f.setTransactionNumber(Collections.frequency(locations, location));
            f.setLocation(location);
            list.add(f);
        }
        list.sort(Comparator.reverseOrder());
        ForReturn forReturn = new ForReturn();
        forReturn.setResponse(list);
        return forReturn;
    }


    public ResponseMessage addTransaction(Integer carrierId, String requestId, String offerId) {
        TransactionDto dto = new TransactionDto(carrierId, requestId, offerId);
        ResponseMessage maker = maker(dto);
        Transaction transaction = (Transaction) maker.getObject();
        if (maker.isSuccess()) {
            transactionRepository.save(transaction);
            maker.setMessage("Saqlandi");
        }
        return maker;
    }

    public ResponseMessage edit(Integer id, TransactionDto transactionDto) {
        Optional<Transaction> optional = transactionRepository.findById(id);
        if (optional.isEmpty())
            return new ResponseMessage("Bunday transaksiya mavjud emas", false);

        ResponseMessage maker = maker(transactionDto);
        if (maker.isSuccess()) {
            Transaction editingTransaction = (Transaction) maker.getObject();
            transactionRepository.save(editingTransaction);
            maker.setMessage("Tahrirlandi");
        }
        return maker;
    }

    public ResponseMessage get(Integer id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        return optionalTransaction.map(transaction -> new ResponseMessage("Transaksiya", true, transaction)).orElseGet(() -> new ResponseMessage(id + " Bunday transaksiya topilmadi", false));
    }

    public ResponseMessage delete(Integer id) {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
        if (optionalTransaction.isEmpty())
            return new ResponseMessage("Transaksiya topilmadi", false);
        transactionRepository.deleteById(id);
        return new ResponseMessage("deleted", true);
    }

    public ResponseMessage evaluate(Integer id, int score) {
        Optional<Transaction> byId = transactionRepository.findById(id);
        if (byId.isEmpty())
            return new ResponseMessage("Transaksiya topilmadi", false);
        Transaction transaction = byId.get();
        transaction.setScore(score);
        Transaction save = transactionRepository.save(transaction);
        if (save.getScore() < 1 || save.getScore() > 10)
            return new ResponseMessage("Ekstrimal", false);

        return new ResponseMessage("Baholandi", true);
    }

}