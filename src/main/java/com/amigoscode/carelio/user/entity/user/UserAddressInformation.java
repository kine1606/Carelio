package com.amigoscode.carelio.user.entity.user;

import com.amigoscode.carelio.base.BaseEntity;
import com.amigoscode.carelio.service_order.entity.ServiceOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "ward")
    private String ward;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "city", nullable = false)
    private String city;

    private boolean isDefault;

    //temporary field
    private String timeAvailability;

    @OneToOne(mappedBy = "userAddressInformation")
    private ServiceOrder serviceOrder;
}
