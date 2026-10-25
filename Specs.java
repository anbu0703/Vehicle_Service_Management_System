package vsms;

/** Table definitions used by the whole application. */
public final class Specs {
    private Specs() {}

    public static final Spec CUSTOMERS = new Spec("Customers", "customers",
        new String[]{"name", "phone", "email", "address", "reg_date"},
        new String[]{"Full Name", "Phone No.", "Email ID", "Address", "Registration Date"}, 2);

    public static final Spec VEHICLES = new Spec("Vehicles", "vehicles",
        new String[]{"vehicle_no", "owner_name", "vehicle_type", "make_model", "fuel_type", "year", "chassis_no", "engine_no", "reg_date"},
        new String[]{"Vehicle No.", "Owner Name", "Vehicle Type", "Make / Model", "Fuel Type", "Year", "Chassis No.", "Engine No.", "Registration Date"}, 4);

    public static final Spec SERVICES = new Spec("Service Entry", "services",
        new String[]{"vehicle_no", "customer_name", "service_type", "service_date", "charges", "status", "kms", "technician", "notes"},
        new String[]{"Vehicle No.", "Customer Name", "Service Type", "Service Date", "Charges (Rs)", "Status", "Kms Reading", "Technician", "Notes"}, 5);

    public static final Spec PAYMENTS = new Spec("Payments", "payments",
        new String[]{"invoice_no", "vehicle_no", "customer_name", "service_type", "amount", "paid_amount", "status", "pay_mode", "pay_date"},
        new String[]{"Invoice No.", "Vehicle No.", "Customer Name", "Service Type", "Amount (Rs)", "Paid (Rs)", "Status", "Payment Mode", "Date"}, 5);

    public static final Spec[] ALL = {CUSTOMERS, VEHICLES, SERVICES, PAYMENTS};

    static {
        VEHICLES.numeric = new String[]{"year"};
        VEHICLES.options.put("vehicle_type", new String[]{"Car", "Bike", "Auto", "Truck"});
        VEHICLES.options.put("fuel_type", new String[]{"Petrol", "Diesel", "CNG", "Electric"});
        SERVICES.numeric = new String[]{"charges", "kms"};
        SERVICES.options.put("service_type", new String[]{"General Service", "Washing", "Oil Change", "Brake Service", "Engine Repair", "Others"});
        SERVICES.options.put("status", new String[]{"Pending", "In Progress", "Completed"});
        PAYMENTS.numeric = new String[]{"amount", "paid_amount"};
        PAYMENTS.options.put("service_type", SERVICES.options.get("service_type"));
        PAYMENTS.options.put("status", new String[]{"Paid", "Partial", "Pending"});
        PAYMENTS.options.put("pay_mode", new String[]{"Cash", "UPI", "Card"});
    }

    public static Spec byTable(String t) {
        for (Spec s : ALL) if (s.table.equals(t)) return s;
        return null;
    }
}
