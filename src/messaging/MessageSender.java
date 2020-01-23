package messaging;

class MessageSender extends Thread {

    private boolean toRun;

    MessageSender(){
        this.toRun = true;
    }

    @Override
    public void run() {
        MessageQueue queue = new MessageQueue();
        while (toRun){
            Message message = queue.getMessage();
            OutputProcessor processor = new OutputProcessor(message);
            processor.sendMessage();
        }
    }
}
