package com.fenix.wakonga.interfaces;


import com.fenix.wakonga.model.Estilos;

import java.util.List;

public interface IFirebaseLoadStore {
    void onFirebaseLoadSucess(List<Estilos> estilosList);
    void onFirebaseLoadFailed(String message);

}
