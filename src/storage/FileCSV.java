package storage;

import model.Phone;
import model.OfficialPhone;
import model.ImportedPhone;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileCSV {
    private static final String FILE_PATH = "mobiles.csv";

    public static List<Phone> readPhonesFromCSV() {
        List<Phone> phoneList = new ArrayList<>();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("File CSV không tồn tại. Tạo file mới...");
            return phoneList; // Trả về danh sách rỗng
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                try {
                    if (data.length == 7) {
                        // Điện thoại chính hãng
                        String id = data[0];
                        String name = data[1];
                        double price = Double.parseDouble(data[2]);
                        int quantity = Integer.parseInt(data[3]);
                        String manufacturer = data[4];
                        int warrantyPeriod = Integer.parseInt(data[5]);
                        String warrantyScope = data[6];
                        phoneList.add(new OfficialPhone(id, name, price, quantity, manufacturer, warrantyPeriod, warrantyScope));
                    } else if (data.length == 6) {
                        // Điện thoại xách tay
                        String id = data[0];
                        String name = data[1];
                        double price = Double.parseDouble(data[2]);
                        int quantity = Integer.parseInt(data[3]);
                        String manufacturer = data[4];
                        String importedCountry = data[5];
                        String status = data[6];
                        phoneList.add(new ImportedPhone(id, name, price, quantity, manufacturer, importedCountry, status));
                    } else {
                        System.out.println("Dòng không hợp lệ: " + line);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Lỗi định dạng dữ liệu ở dòng: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file CSV: " + e.getMessage());
        }

        return phoneList;
    }



    public static void writePhonesToCSV(List<Phone> phoneList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Phone phone : phoneList) {
                writer.write(phone.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file CSV: " + e.getMessage());
        }
    }
}

