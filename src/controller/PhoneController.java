package controller;

import exception.NotFoundProductException;
import model.OfficialPhone;
import model.ImportedPhone;
import model.Phone;
import storage.FileCSV;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PhoneController {
    private List<Phone> phoneList = new ArrayList<>();
    public PhoneController() {
        phoneList = FileCSV.readPhonesFromCSV();
    }

    public void savePhonesToFile() {
        FileCSV.writePhonesToCSV(phoneList);
    }



    public void addPhoneFromInput(Scanner scanner) {
        String id = phoneList.isEmpty()
                ? "1"
                : String.valueOf(Integer.parseInt(phoneList.get(phoneList.size() - 1).getId()) + 1);

        System.out.println("ID tự động gán cho điện thoại mới: " + id);

        System.out.println("Chọn loại điện thoại:");
        System.out.println("1. Điện thoại chính hãng");
        System.out.println("2. Điện thoại xách tay");
        int type = scanner.nextInt();
        scanner.nextLine();

        // Nhập thông tin chung
        System.out.print("Nhập tên điện thoại: ");
        String name = scanner.nextLine().trim();
        while (name.isEmpty()) {
            System.out.println("Tên điện thoại là bắt buộc. Vui lòng nhập lại.");
            System.out.print("Nhập tên điện thoại: ");
            name = scanner.nextLine().trim();
        }

        double price = -1;
        while (price <= 0) {
            System.out.print("Nhập giá bán (số dương): ");
            if (scanner.hasNextDouble()) {
                price = scanner.nextDouble();
                if (price <= 0) {
                    System.out.println("Giá bán phải là số dương. Vui lòng nhập lại.");
                }
            } else {
                System.out.println("Giá bán không hợp lệ. Vui lòng nhập lại.");
                scanner.next();
            }
        }

        int quantity = -1;
        while (quantity <= 0) {
            System.out.print("Nhập số lượng (số dương): ");
            if (scanner.hasNextInt()) {
                quantity = scanner.nextInt();
                if (quantity <= 0) {
                    System.out.println("Số lượng phải là số dương. Vui lòng nhập lại.");
                }
            } else {
                System.out.println("Số lượng không hợp lệ. Vui lòng nhập lại.");
                scanner.next();
            }
        }
        scanner.nextLine();

        System.out.print("Nhập nhà sản xuất: ");
        String manufacturer = scanner.nextLine().trim();
        while (manufacturer.isEmpty()) {
            System.out.println("Nhà sản xuất là bắt buộc. Vui lòng nhập lại.");
            System.out.print("Nhập nhà sản xuất: ");
            manufacturer = scanner.nextLine().trim();
        }

        // Xử lý thông tin theo loại điện thoại
        if (type == 1) {
            // Điện thoại chính hãng
            int warrantyPeriod = -1;
            while (warrantyPeriod <= 0 || warrantyPeriod > 730) {
                System.out.print("Nhập thời gian bảo hành (tối đa 730 ngày): ");
                if (scanner.hasNextInt()) {
                    warrantyPeriod = scanner.nextInt();
                    if (warrantyPeriod <= 0 || warrantyPeriod > 730) {
                        System.out.println("Thời gian bảo hành phải là số dương và không quá 730 ngày. Vui lòng nhập lại.");
                    }
                } else {
                    System.out.println("Thời gian bảo hành không hợp lệ. Vui lòng nhập lại.");
                    scanner.next();
                }
            }
            scanner.nextLine();

            System.out.print("Nhập phạm vi bảo hành (Toan Quoc/Quoc Te): ");
            String warrantyScope = scanner.nextLine().trim();
            while (!warrantyScope.equals("Toan Quoc") && !warrantyScope.equals("Quoc Te")) {
                System.out.println("Phạm vi bảo hành không hợp lệ. Chỉ chấp nhận 'Toan Quoc' hoặc 'Quoc Te'.");
                System.out.print("Nhập phạm vi bảo hành (Toan Quoc/Quoc Te): ");
                warrantyScope = scanner.nextLine().trim();
            }

            phoneList.add(new OfficialPhone(id, name, price, quantity, manufacturer, warrantyPeriod, warrantyScope));
        } else if (type == 2) {
            // Điện thoại xách tay
            System.out.print("Nhập quốc gia xách tay: ");
            String importedCountry = scanner.nextLine().trim();
            while (importedCountry.equals("Viet Nam")) {
                System.out.println("Quốc gia xách tay không được là 'Viet Nam'. Vui lòng nhập lại.");
                System.out.print("Nhập quốc gia xách tay: ");
                importedCountry = scanner.nextLine().trim();
            }

            System.out.print("Nhập trạng thái (Da sua chua/Chua sua chua): ");
            String status = scanner.nextLine().trim();
            while (!status.equals("Da sua chua") && !status.equals("Chua sua chua")) {
                System.out.println("Trạng thái không hợp lệ. Chỉ chấp nhận 'Da sua chua' hoặc 'Chua sua chua'.");
                System.out.print("Nhập trạng thái (Da sua chua/Chua sua chua): ");
                status = scanner.nextLine().trim();
            }

            phoneList.add(new ImportedPhone(id, name, price, quantity, manufacturer, importedCountry, status));
        } else {
            System.out.println("Loại điện thoại không hợp lệ!");
            return;
        }
        savePhonesToFile();
        System.out.println("Đã thêm điện thoại thành công với ID: " + id);
    }


    // Xóa điện thoại
    public void deletePhoneById(Scanner scanner) {
        System.out.print("Nhập ID điện thoại cần xóa: ");
        String id = scanner.nextLine().trim();

        // Tìm điện thoại trong danh sách
        Phone phone = phoneList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        // Nếu không tìm thấy, ném ngoại lệ
        if (phone == null) {
            try {
                throw new NotFoundProductException("ID điện thoại không tồn tại.");
            } catch (NotFoundProductException e) {
                System.out.println(e.getMessage());
            }
            return; // Quay lại menu chính
        }

        // Yêu cầu xác nhận xóa
        System.out.print("Bạn có chắc chắn muốn xóa điện thoại này? (Yes/No): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Yes")) {
            // Xóa điện thoại khỏi danh sách
            phoneList.remove(phone);

            // Ghi lại danh sách vào file CSV
            savePhonesToFile();

            System.out.println("Đã xóa điện thoại thành công!");
            displayAllPhones(); // Hiển thị lại danh sách sau khi xóa
        } else {
            System.out.println("Hủy bỏ thao tác xóa. Quay lại menu chính.");
        }
    }

    // Hiển thị danh sách điện thoại
    public void displayAllPhones() {
        if (phoneList.isEmpty()) {
            System.out.println("Danh sách điện thoại rỗng.");
        } else {
            for (Phone phone : phoneList) {
                phone.displayInfo();
                System.out.println();
            }
        }
    }

    // Tìm kiếm điện thoại theo ID
    public void searchPhoneById(Scanner scanner) {
        System.out.print("Nhập ID điện thoại cần tìm: ");
        String id = scanner.nextLine();
        Phone phone = phoneList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (phone != null) {
            phone.displayInfo();
        } else {
            System.out.println("Không tìm thấy điện thoại với ID: " + id);
        }
    }
}
