package com.walichin.archivoalejos.carganegativos.adapters;

import com.walichin.archivoalejos.carganegativos.models.Item;

/**
 * Created by lenovo on 2/23/2016.
 */
public interface ItemClickListener {
    void itemClicked(Item item);
    void itemClicked(Section section);
}
