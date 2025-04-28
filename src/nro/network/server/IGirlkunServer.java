package nro.network.server;

public interface IGirlkunServer extends Runnable {
  IGirlkunServer init();
  
  IGirlkunServer start(int paramInt) throws Exception;
  
  IGirlkunServer setAcceptHandler(ISessionAcceptHandler paramISessionAcceptHandler);
  
  IGirlkunServer close();
  
  IGirlkunServer dispose();
  
  IGirlkunServer randomKey(boolean paramBoolean);
  
  IGirlkunServer setDoSomeThingWhenClose(IServerClose paramIServerClose);
  
  IGirlkunServer setTypeSessioClone(Class paramClass) throws Exception;
  
  ISessionAcceptHandler getAcceptHandler() throws Exception;
  
  boolean isRandomKey();
  
  void stopConnect();
}


/* Location:              C:\Users\VoHoangKiet\Downloads\TEA_V5\lib\GirlkunNetwork.jar!\com\girlkun\network\server\IGirlkunServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */