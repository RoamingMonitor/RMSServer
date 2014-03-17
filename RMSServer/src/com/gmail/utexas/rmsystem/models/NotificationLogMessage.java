package com.gmail.utexas.rmsystem.models;

public class NotificationLogMessage {
		public String messageTitle;
		public String dateAndTime;
		public String messageBody;
		
		public NotificationLogMessage(){} //Empty Constructor used for testing
		
		public NotificationLogMessage(String title, String date, String body){
			messageTitle = title;
			dateAndTime = date;
			messageBody = body;			
		}
		

	    //Getter and Setter methods used for testing
	    /*********** Set Methods ******************/
        
        public void setMessageTitle(String messageTitle)
        {
            this.messageTitle = messageTitle;
        }
         
        public void setDateAndTime(String dateAndTime)
        {
            this.dateAndTime = dateAndTime;
        }
         
        public void setMessageBody(String messageBody)
        {
            this.messageBody = messageBody;
        }
         
        /*********** Get Methods ****************/
         
        public String getMessageTitle()
        {
            return this.messageTitle;
        }
         
        public String getDateAndTime()
        {
            return this.dateAndTime;
        }
         
        public String getMessageBody()
        {
            return this.messageBody;
        }
}
