package com.hotel.manager.client;

import com.hotel.manager.client.view.MainView;
import com.hotel.manager.core.wiring.CoreModule;
import javax.swing.SwingUtilities;

public class HotelManagerClientApplication {

    public static void main(String[] args) {
        CoreModule.initialize();
        SwingUtilities.invokeLater(MainView::new);
    }
}