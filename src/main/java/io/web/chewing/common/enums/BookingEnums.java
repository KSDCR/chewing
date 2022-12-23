package io.web.chewing.common.enums;

public class BookingEnums {

    public enum BookingState {
        needConfirm("예약대기"),
        agreedConfirm("예약확인"),
        disagreeConfirm("예약거절"),
        Complete("이용완료");

        private final String state;

        BookingState(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }
    }
}
