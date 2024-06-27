package com.lpb.mid.exception;

public enum ErrorMessage {
    INCORRECT_USERNAME_PASSWORD("30", "The Username or Password is Incorrect", "1"),
    ERR_000("00", "Successful", "0"),
    ERR_10("10", "Hệ thống đang trong giai đoạn nâng cấp", "1"),
    ERR_11("11", "Thông tin đăng nhập không chính xác", "1"),
    ERR_12("12", "Version API không được hỗ trợ", "1"),
    ERR_13("13", "Token không hợp lệ", "1"),
    ERR_14("14", "White list IP chưa được đăng ký", "1"),
    ERR_20("20", "Request sai định dạng ", "1"),
    ERR_21("21", "HMAC không hợp lệ", "1"),
    ERR_22("22", "Số tiền không hợp lệ", "1"),
    ERR_23("23", "Tài khoản không hợp lệ", "1"),
    ERR_24("24", "Giao dịch tương tự đang xử lý, vui lòng không thực hiện lại", "1"),
    ERR_25("25", "Hết hạn mức giao dịch ", "1"),
    ERR_26("26", "Số lượng request không hợp lê, thực hiện lại thời điểm khác", "1"),
    ERR_27("27", "Giao dịch không tồn tại trên hệ thống", "1"),
    ERR_28("28", "Mã giao dịch không tồn tại", "1"),
    ERR_29("29", "Tài khoản không tồn tại", "1"),
    ERR_30("30", "Tài khoản chặn ghi có", "1"),
    ERR_31("31", "Tài khoản không phải tài khoản thanh toán", "1"),
    ERR_32("32", "Thẻ hoặc khuôn mặt không hợp lệ", "1"),
    ERR_33("33", "Thông tin dữ liệu không hợp lệ", "1"),
    ERR_34("34", "Xác thực EKYC thất bại", "1"),
    ERR_35("35", "Xác thực faces thất bại", "1"),
    ERR_36("36", "Độ dài transaction ID không hợp lệ", "1"),
    ERR_37("37", "IdNumber đã tồn tại trên hệ thống", "1"),
    ERR_38("38", "Tài khoản đã tồn tại trên hệ thống", "1"),
    ERR_39("39", "Compare Ocr Nfc thất bại", "1"),
    ERR_40("40", "Xác thực rar thất bại", "1"),
    ERR_41("41", "Ocr Plus dữ liệu nâng cao thất bại", "1"),
    ERR_42("42", "Không tìn thấy thông tin người dùng", "1"),
    ERR_43("43", "Người dùng chưa kích hoạt", "1"),
    ERR_44("44", "Cập nhật dữ liệu ocr thất bại", "1"),
    ERR_45("45", "Kích hoạt ekyc thất bại", "1"),
    ERR_46("46", "Đăng nhập ekyc core thất bại", "1"),
    ERR_47("47", "Dữ liệu mobile không hợp lệ", "1"),
    //tk ảo
    ERR_61("61", "User không tồn tại trên hệ thống", "1"),
    ERR_63("63", "Tài khoản ảo đã tồn tại", "1"),
    ERR_62("62", "prefix không tồn tại trên hệ thống", "1"),
    ERR_64("64", "Tạo tài khoản ảo thất bại", "1"),
    ERR_65("65", "Trạng thái tài khoản không hợp lệ.", "1"),
    ERR_90("90", "Giao dịch đang xử lý", "2"),
    ERR_91("91", "Giao dịch thất bại.", "1"),
    ERR_99("99", "Lỗi hệ thống", "1");
    public final String code;
    public final String message;
    public final String type;

    ErrorMessage(String code, String message, String type) {
        this.code = code;
        this.message = message;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

}
