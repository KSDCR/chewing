package io.web.chewing.domain.booking;

public class BookingState {

    public enum bookingState {
        needConfirm("예약대기"),
        agreedConfirm("예약확인"),
        disagreeConfirm("예약거절"),
        Complete("이용완료");

        private final String state;

        bookingState(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }
    }
}
