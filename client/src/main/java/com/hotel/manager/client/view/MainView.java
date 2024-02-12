package com.hotel.manager.client.view;

import com.hotel.manager.core.domain.Booking;
import com.hotel.manager.core.domain.Guest;
import com.hotel.manager.core.domain.Room;
import com.hotel.manager.core.exception.NoAvailableRoomsException;
import com.hotel.manager.core.service.BookingService;
import com.hotel.manager.core.service.GuestService;
import com.hotel.manager.core.service.RoomService;
import com.hotel.manager.core.service.dto.RoomLookupParams;
import com.hotel.manager.core.wiring.BookingServiceFactory;
import com.hotel.manager.core.wiring.GuestServiceFactory;
import com.hotel.manager.core.wiring.RoomServiceFactory;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

// TODO - externalize strings.
// TODO - split this class into separate components.
// TODO - avoid executing unnecessary queries.
public class MainView extends JFrame {

    private static final String[] ROOMS_TABLE_COLUMNS = {"Number", "Status"};
    private static final String[] GUEST_TABLE_COLUMNS = {"Name", "Surname", "Check-in Time"};
    private static final String[] HISTORY_TABLE_COLUMNS = {"Name", "Surname", "Check-in Time", "Check-out Time"};

    private final RoomService roomService = RoomServiceFactory.getInstance();
    private final BookingService bookingService = BookingServiceFactory.getInstance();
    private final GuestService guestService = GuestServiceFactory.getInstance();

    private List<Room> rooms = new ArrayList<>();
    private Room selectedRoom;

    private JTextField number;
    private JComboBox<String> status;
    private JTable roomsTable;
    private JTable guestsTable;
    private JTable historyTable;
    private JButton checkout;
    private JTextField checkinName;
    private JTextField checkinSurname;

    public MainView() {
        initComponents();
    }

    private void roomsTabComponentShown() {
        populateRoomsTable();
    }

    private void populateRoomsTable() {
        rooms = roomService.getAllRooms();
        populateRoomsTable(rooms);
    }

    private void populateRoomsTable(List<Room> rooms) {
        this.rooms = rooms;

        Object[][] data = new Object[rooms.size()][2];

        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            data[i][0] = room.getNumber();
            data[i][1] = room.isAvailable() ? "Available" : "Occupied";
        }

        roomsTable.setModel(new DefaultTableModel(data, ROOMS_TABLE_COLUMNS));

        if (!rooms.isEmpty()) {
            selectedRoom = rooms.get(0);
            roomsTable.setRowSelectionInterval(0, 0);
        } else {
            selectedRoom = null;
        }
    }

    private void roomsTableMouseClicked(MouseEvent e) {
        int selectedRow = ((JTable) e.getSource()).getSelectedRow();
        selectedRoom = rooms.get(selectedRow);
        populateGuestsTab();
        populateHistoryTable();
    }

    private void guestsTabComponentShown() {
        populateGuestsTab();
    }

    private void populateGuestsTab() {
        if (selectedRoom == null || selectedRoom.isAvailable()) {
            guestsTable.setModel(new DefaultTableModel(GUEST_TABLE_COLUMNS, 0));
            checkout.setEnabled(false);
            return;
        }

        Booking booking = bookingService.getActiveBookingByRoomId(selectedRoom.getId());

        Guest guest = guestService.getGuestById(booking.getGuestId());

        Object[][] data = new Object[1][3];
        data[0][0] = guest.getName();
        data[0][1] = guest.getSurname();
        data[0][2] = formatLocalDateTime(booking.getCheckinTime());

        checkout.setEnabled(true);

        guestsTable.setModel(new DefaultTableModel(data, GUEST_TABLE_COLUMNS));
    }

    private void historyTabComponentShown() {
        populateHistoryTable();
    }

    private void populateHistoryTable() {
        if (selectedRoom == null) {
            historyTable.setModel(new DefaultTableModel(HISTORY_TABLE_COLUMNS, 0));
            return;
        }

        List<Booking> bookings = bookingService.getCompletedBookingsByRoomId(selectedRoom.getId());

        Object[][] data = new Object[bookings.size()][4];

        for (int i = 0; i < bookings.size(); i++) {
            Booking booking = bookings.get(i);
            Guest guest = guestService.getGuestById(booking.getGuestId());
            data[i][0] = guest.getName();
            data[i][1] = guest.getSurname();
            data[i][2] = formatLocalDateTime(booking.getCheckinTime());
            data[i][3] = formatLocalDateTime(booking.getCheckoutTime());
        }

        historyTable.setModel(new DefaultTableModel(data, HISTORY_TABLE_COLUMNS));
    }

    private String formatLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return localDateTime.format(formatter);
    }

    private void checkinClicked() {
        if (!isCheckinFormFilled()) {
            JOptionPane.showMessageDialog(new Frame(), "Name and surname must be provided.");
        }

        Guest guest = new Guest();
        guest.setName(checkinName.getText());
        guest.setSurname(checkinSurname.getText());

        try {
            Room room = roomService.checkin(guest);

            JOptionPane.showMessageDialog(new Frame(), String.format("Guest check-in completed, room No. %s.", room.getNumber()));

            checkinName.setText("");
            checkinSurname.setText("");

            populateRoomsTable();
            populateGuestsTab();
            populateHistoryTable();
        } catch (NoAvailableRoomsException exception) {
            JOptionPane.showMessageDialog(new Frame(), "There are no available rooms at the moment.");
        }
    }

    private boolean isCheckinFormFilled() {
        String name = checkinName.getText();
        String surname = checkinSurname.getText();

        return name != null && surname != null && !name.isBlank() && !surname.isBlank();
    }

    private void checkoutMouseClicked() {
        roomService.checkout(selectedRoom.getId());

        JOptionPane.showMessageDialog(new Frame(), "Guest check-out completed.");

        populateRoomsTable();
        populateGuestsTab();
        populateHistoryTable();
    }

    private void searchMouseClicked() {
        RoomLookupParams params = new RoomLookupParams();

        if (!number.getText().isBlank()) {
            params.setNumber(number.getText());
        }

        if (!"Any".equals(status.getSelectedItem())) {
            params.setStatus(String.valueOf(status.getSelectedItem()).toUpperCase());
        }

        this.rooms = roomService.lookupRooms(params);

        populateRoomsTable(rooms);
        populateGuestsTab();
        populateHistoryTable();
    }

    private void thisWindowOpened() {
        populateRoomsTable();
        populateGuestsTab();
        populateHistoryTable();
    }

    private void initComponents() {
        number = new JTextField();
        status = new JComboBox<>();
        roomsTable = new JTable();
        guestsTable = new JTable();
        historyTable = new JTable();
        checkout = new JButton();
        checkinName = new JTextField();
        checkinSurname = new JTextField();

        JTabbedPane tabs = new JTabbedPane();
        JTabbedPane roomDetailsView = new JTabbedPane();
        JPanel roomsTab = new JPanel();
        JPanel guestsTab = new JPanel();
        JPanel searchForm = new JPanel();
        JPanel checkinTab = new JPanel();
        JLabel numberLabel = new JLabel();
        JLabel statusLabel = new JLabel();
        JLabel nameLabel = new JLabel();
        JLabel surnameLabel = new JLabel();
        JButton search = new JButton();
        JButton submit = new JButton();
        JSplitPane searchResultsView = new JSplitPane();
        JScrollPane roomsListView = new JScrollPane();
        JScrollPane guestsList = new JScrollPane();
        JScrollPane historyTab = new JScrollPane();
        JToolBar guestsActionBar = new JToolBar();

        setTitle("Hotel Manager");
        setVisible(true);
        setPreferredSize(new Dimension(1200, 800));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                thisWindowOpened();
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout());

        roomsTab.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                roomsTabComponentShown();
            }
        });
        roomsTab.setLayout(new BorderLayout());

        numberLabel.setText("Number");

        number.setMaximumSize(new Dimension(2147483647, 30));
        number.setToolTipText("Room number");

        statusLabel.setText("Status");

        status.setModel(new DefaultComboBoxModel<>(new String[]{
                "Any",
                "Available",
                "Occupied"
        }));
        status.setMaximumSize(new Dimension(32767, 30));
        status.setToolTipText("Room status");

        search.setText("Search");
        search.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchMouseClicked();
            }
        });

        GroupLayout searchFormLayout = new GroupLayout(searchForm);
        searchForm.setLayout(searchFormLayout);
        searchFormLayout.setHorizontalGroup(
                searchFormLayout.createParallelGroup()
                        .addGroup(searchFormLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(searchFormLayout.createParallelGroup()
                                        .addGroup(searchFormLayout.createSequentialGroup()
                                                .addComponent(numberLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(number, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, searchFormLayout.createSequentialGroup()
                                                .addComponent(statusLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(status, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, searchFormLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(search)))
                                .addContainerGap())
        );
        searchFormLayout.setVerticalGroup(
                searchFormLayout.createParallelGroup()
                        .addGroup(searchFormLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(searchFormLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(number, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(numberLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(searchFormLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(status, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(statusLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(search)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        roomsTab.add(searchForm, BorderLayout.WEST);

        searchResultsView.setOrientation(JSplitPane.VERTICAL_SPLIT);
        searchResultsView.setDividerLocation(200);

        roomsListView.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                roomsTabComponentShown();
            }
        });

        roomsTable.setModel(new DefaultTableModel(new Object[][]{}, ROOMS_TABLE_COLUMNS));
        roomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomsTable.setAutoCreateRowSorter(true);
        roomsTable.setFillsViewportHeight(true);
        roomsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                roomsTableMouseClicked(e);
            }
        });
        roomsListView.setViewportView(roomsTable);

        searchResultsView.setTopComponent(roomsListView);

        guestsTab.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                guestsTabComponentShown();
            }
        });
        guestsTab.setLayout(new BorderLayout());

        guestsTable.setModel(new DefaultTableModel(new Object[][]{}, GUEST_TABLE_COLUMNS));
        guestsList.setViewportView(guestsTable);

        guestsTab.add(guestsList, BorderLayout.CENTER);

        guestsActionBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        checkout.setText("Checkout");
        checkout.setFocusable(false);
        checkout.setEnabled(false);
        checkout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkoutMouseClicked();
            }
        });
        guestsActionBar.add(checkout);

        guestsTab.add(guestsActionBar, BorderLayout.NORTH);

        roomDetailsView.addTab("Guests", guestsTab);

        historyTab.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                historyTabComponentShown();
            }
        });

        historyTable.setModel(new DefaultTableModel(new Object[][]{}, HISTORY_TABLE_COLUMNS));
        historyTab.setViewportView(historyTable);

        roomDetailsView.addTab("History", historyTab);

        searchResultsView.setBottomComponent(roomDetailsView);

        roomsTab.add(searchResultsView, BorderLayout.CENTER);

        tabs.addTab("Rooms", roomsTab);

        nameLabel.setText("Name");

        checkinName.setMinimumSize(new Dimension(50, 30));

        surnameLabel.setText("Surname");

        checkinSurname.setMinimumSize(new Dimension(50, 30));

        submit.setText("Submit");
        submit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                checkinClicked();
            }
        });

        GroupLayout checkinTabLayout = new GroupLayout(checkinTab);
        checkinTab.setLayout(checkinTabLayout);
        checkinTabLayout.setHorizontalGroup(
                checkinTabLayout.createParallelGroup()
                        .addGroup(checkinTabLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(checkinTabLayout.createParallelGroup()
                                        .addGroup(checkinTabLayout.createSequentialGroup()
                                                .addComponent(surnameLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED))
                                        .addGroup(GroupLayout.Alignment.TRAILING, checkinTabLayout.createSequentialGroup()
                                                .addComponent(nameLabel)
                                                .addGap(31, 31, 31)))
                                .addGroup(checkinTabLayout.createParallelGroup()
                                        .addComponent(checkinName, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(checkinTabLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addComponent(submit)
                                                .addComponent(checkinSurname, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(637, Short.MAX_VALUE))
        );
        checkinTabLayout.setVerticalGroup(
                checkinTabLayout.createParallelGroup()
                        .addGroup(checkinTabLayout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addGroup(checkinTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(nameLabel)
                                        .addComponent(checkinName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(checkinTabLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(surnameLabel)
                                        .addComponent(checkinSurname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(submit)
                                .addContainerGap(327, Short.MAX_VALUE))
        );

        tabs.addTab("Check-in", checkinTab);

        contentPane.add(tabs);
        pack();
        setLocationRelativeTo(null);
    }
}
