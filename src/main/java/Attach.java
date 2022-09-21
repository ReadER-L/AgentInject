import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;

public class Attach {
    public static String CLASS_NAME = "org.apache.catalina.startup.Bootstrap";
    public static void main(String[] args){

        String property = System.getProperty("user.dir");
        String agentPath = property+"/AgentInject-1.0-SNAPSHOT-jar-with-dependencies.jar";
        try {
            Class.forName("sun.tools.attach.HotSpotAttachProvider");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        System.out.println(agentPath);
        for (VirtualMachineDescriptor virtualMachineDescriptor : list){
//            System.out.println(virtualMachineDescriptor.displayName());
            if (virtualMachineDescriptor.displayName().contains(CLASS_NAME)){
                String id = virtualMachineDescriptor.id();
                VirtualMachine attach = null;
                try {
                    System.out.println(id);
                    attach = VirtualMachine.attach(id);
                    attach.loadAgent(agentPath);
                } catch (AttachNotSupportedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (AgentLoadException e) {
                    e.printStackTrace();
                } catch (AgentInitializationException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        attach.detach();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("success");
            }
        }
    }
}