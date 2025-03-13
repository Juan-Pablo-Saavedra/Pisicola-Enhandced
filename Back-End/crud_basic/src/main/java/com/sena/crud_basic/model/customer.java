package com.sena.crud_basic.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "customer")
public class customer {
   /*
     * @ID = es una llave primaria o PK
     */
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @Column (name = "name",length = 100, nullable = false)
    private String name;

    @Column (name = "email", length = 255, nullable = false)
    private String email;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

 public customer( int id, String name, String email, String phone){
 this.id = id;
 this.name = name;
 this. email = email;
 this.phone = phone;
 }

public int getid (){
    return id;
}

public void  setid (int id){
    this.id = id;
}

public String getname (){
    return name;
}

public void setname (String name){
    this.name = name;
}

public String getemail (){
    return email;
}

public void setemail (String email){
    this.email = email;
}

public String getphone (){
    return phone;
}

public void setphone (String phone) {
    this.phone = phone;
}





}
