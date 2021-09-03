/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ada.system.mass.messaging.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.ada.system.mass.messaging.domain.service.IHomeService;

/**
 *
 * @author Xiangn Rodriguez<xiangnrodriguez@gmail.com>
 */
public class HomeServiceImpl implements IHomeService {

    @Override
    public List<String> infoCompany() {
        List<String> info = new ArrayList<>();
        String company = "Fibernet, C.A.";
        String rif = "J - 41225817-6";
        String adress = "Calle 54 entre Av Pedro Leon Torres y carrera 21, local No 48";
        String phone = "+58424-572-5510";
        info.add(company);
        info.add(rif);
        info.add(adress);
        info.add(phone);
        return info;
    }

}
