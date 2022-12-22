package com.moutamid.cogear.utilis;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Constants {

    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApB3Pb51I49Y+CBRl92py1a00hyE7TDj0G/Iv2/mFT1enQWipoD0Jodvfv2AgipRM87hm0XJYhBV2ovYy6HWYp0tVMNT33jIa+kBJZGH9pkMJHvIkBQy+0XndSHIc95QiLC9iZddSYDGqzBcwmzyBBOwcPdBdF0G9B+mx+fz+XVTgn3mN6tk8jIFjU+M7kIOo4/E7qyFtM5tNzNOIVlxgB2mtg9siSSx8yaR11PCjHpaYxxFJd3tyEUomVV83KN7sPbTjgP008ziCx5532eCVt/8o5ZOGJaddHDuKCQEcKlgaM3zyEa9Kuk+QxHh5TqHWpNVKdeLQ6X033kd+pJqBQQIDAQAB";
    public static final String TWO_HUNDRED_DOLLAR_PRODUCT = "two.hundred.com.moutamid.cogear";
    public static final String TWO_TWENTY_FIVE_DOLLAR_PRODUCT = "two.twenty.five.com.moutamid.cogear";
    public static final String TWO_FORTY_SIX_DOLLAR_PRODUCT = "two.forty.six.com.moutamid.cogear";
    public static final String TWO_SIXTY_FIVE_DOLLAR_PRODUCT = "two.sixty.five.com.moutamid.cogear";
    public static final String THREE_HUNDRED_DOLLAR_PRODUCT = "three.hundred.com.moutamid.cogear";

    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("CoGear");
        db.keepSynced(true);
        return db;
    }

    public static StorageReference storageReference(String auth){
        StorageReference sr = FirebaseStorage.getInstance().getReference().child("CoGear").child(auth);
        return sr;
    }
}
