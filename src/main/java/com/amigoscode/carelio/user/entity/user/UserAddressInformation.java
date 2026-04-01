package com.amigoscode.carelio.user.entity.user;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.serviceOrder.entity.ServiceOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_address_information")
@NoArgsConstructor
@AllArgsConstructor
public class UserAddressInformation extends BaseEntity
{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "contact_name", nullable = false)
    private String contactName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "address_line", nullable = false)
    private String addressLine;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "userAddressInformation")
    private List<ServiceOrder> serviceOrders;
}
