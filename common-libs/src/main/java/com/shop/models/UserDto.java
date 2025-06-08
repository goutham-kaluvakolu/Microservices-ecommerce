package com.shop.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
//     JSON
// {
//   "id": "string (50 chars)",
//   "email": "string (255 chars, unique)",
//   "firstName": "string (100 chars)",
//   "lastName": "string (100 chars)",
//   "phoneNumber": "string (20 chars)",
//   "defaultShippingAddress": "Address",
//   "defaultBillingAddress": "Address",
//   "createdAt": "timestamp",
//   "updatedAt": "timestamp"
// }

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
