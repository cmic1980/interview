package pro.caifu365.interview.exception;

public class ExceptionTest {
    public static void main(String[] args) {

        ExceptionTest exceptionTest = new ExceptionTest();

        exceptionTest.exe();;
    }

    class MyException extends RuntimeException {
        /**
         * 错误编码
         */
        private String errorCode;


        public MyException(){}

        /**
         * 构造一个基本异常.
         *
         * @param message
         *        信息描述
         */
        public MyException(String message)
        {
            super(message);
        }



        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }
    }

    public void exe(){
        try
        {
            MyException test = new MyException();
            throw test;
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

}
