package com.example.delivery.service;

import com.example.delivery.entity.Carrier;
import com.example.delivery.entity.Location;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.repository.CarrierRepository;
import com.example.delivery.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CarrierService {

    @Autowired
    CarrierRepository carrierRepository;
    @Autowired
    LocationRepository locationRepository;

    /**
     * Carrier ni tizimga qo’shish, bunda carrier ning ismi va u
     * xizmat ko’rsatadigan hududlar nomlari (regionNames) tizimga
     * jo’natiladi. Tizimda mavjud bo’lmagan va duplicate hudud
     * nomlari etiborga olinmaydi. Api method carrier xizmat
     * qiladigan hududlarning tartiblangan ro’yxatini qaytaradi.
     */
    public ResponseMessage addCarrier(String name, List<String> regionNames) {

        Set<Location> locations = new TreeSet<>();
        Set<String> regions = new HashSet<>(regionNames);

        for (String regionName : regions) {
            List<Location> dbLoc = locationRepository.findByRegion(regionName);
            if (dbLoc.isEmpty())
                return new ResponseMessage("Siz kiritgan hududlar topilmadi", false);
            locations.addAll(dbLoc);
        }
        List<Location> list = new ArrayList<>(locations);

        Carrier carrier = new Carrier();
        carrier.setName(name);
        carrier.setLocations(list);
        carrierRepository.save(carrier);
        return new ResponseMessage("",true,locations);
    }

    /**
     * Tizimga hudud nomi (regionName) jo’natilganda shu hududga
     * xizmat qiladigan carrier lar ning tartiblangan (carrierning
     * ismi bo’yicha) ro’yxatini qaytaradi
     */
    public ResponseMessage getCarriersFromRegion(String region) {

        List<Location> dbLoc = locationRepository.findByRegion(region);
        if (dbLoc.isEmpty())
            return new ResponseMessage("bu hududda tashuvchi topilmadi",false);

        List<List<Carrier>> result = new ArrayList<>();
        for (Location location : dbLoc) {
            if (!carrierRepository.findByLocationsEquals(location).isEmpty()) {
                List<Carrier> carriers = carrierRepository.findByLocationsEquals(location);
                carriers.sort(Comparator.comparing(Carrier::getName));
                result.add(carriers);
            }
        }

        return new ResponseMessage(true,result);
    }


    public ResponseMessage getCarriers(Integer carrierId) {
        List<Carrier> all = carrierRepository.findAll();
        return new ResponseMessage("Barcha tashuvchilar", true, all);
    }

    public ResponseMessage delete(Integer id) {
        carrierRepository.deleteById(id);
        return new ResponseMessage("Tashuvchi o'chirildi", true);
    }


}
