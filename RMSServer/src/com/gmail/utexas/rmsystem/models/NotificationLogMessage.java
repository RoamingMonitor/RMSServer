package com.gmail.utexas.rmsystem.models;

public class NotificationLogMessage {
		private String messageTitle;
		private String dateAndTime;
		private String messageBody;
		private String alertType;
		
		public NotificationLogMessage(){} //Empty Constructor used for testing
		
		public NotificationLogMessage(String title, String date, String body, String type){
			messageTitle = title;
			dateAndTime = date;
			messageBody = body;			
			alertType = type;
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

		public void setAlertType(String type) {
			this.alertType = type;
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

		public String getAlertType() {
			return alertType;
		}
}
