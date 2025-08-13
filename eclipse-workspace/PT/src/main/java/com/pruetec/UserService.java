package com.pruetec;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserService {

    public static boolean isValidRFC(String rfc) {
        
        String regex = "^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}$";
        return Pattern.compile(regex).matcher(rfc).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone.matches("^\\+\\d{1,3}\\s\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{2}$");
    }

    public static User createUser(DataRequest req) {
        if (!isValidRFC(req.taxId)) {
            throw new IllegalArgumentException("RFC inválido");
        }
        if (!isValidPhone(req.phone)) {
            throw new IllegalArgumentException("Teléfono inválido");
        }

        User u = new User();
        u.id = UUID.randomUUID();
        u.email = req.email;
        u.name = req.name;
        u.phone = req.phone;
        u.password = CryptoUtil.encrypt(req.password);
        u.taxId = req.taxId;
        u.createdAt = TimeUtil.nowMadagascar();
        u.addresses = req.addresses;

        Database.insertUser(u);
        return u;
    }

    public static List<DataResponse> getUsers(String sortBy, String filterField, String operator, String value) {
        List<User> users = Database.getAllUsers();

        if (filterField != null && operator != null && value != null) {
            users = users.stream().filter(u -> {
                String fieldVal = getFieldValue(u, filterField);
                if (fieldVal == null) return false;

                switch (operator) {
                    case "co": return fieldVal.contains(value);
                    case "eq": return fieldVal.equals(value);
                    case "sw": return fieldVal.startsWith(value);
                    case "ew": return fieldVal.endsWith(value);
                    default: return true;
                }
            }).collect(Collectors.toList());
        }

        if (sortBy != null) {
            users.sort(Comparator.comparing(u -> getFieldValue(u, sortBy)));
        }

        return users.stream().map(DataResponse::from).collect(Collectors.toList());
    }

    private static String getFieldValue(User u, String field) {
        switch (field) {
            case "email": return u.email;
            case "name": return u.name;
            case "phone": return u.phone;
            case "taxId": return u.taxId;
            case "createdAt": return u.createdAt;
            default: return null;
        }
    }
}
