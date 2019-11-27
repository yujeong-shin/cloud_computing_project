package aws;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.DeleteKeyPairRequest;
import com.amazonaws.services.ec2.model.DeleteKeyPairResult;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.KeyPairInfo;
import com.amazonaws.services.ec2.model.MonitorInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesResult;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.UnmonitorInstancesRequest;
import com.amazonaws.services.ec2.model.Filter;

public class awsTest {

static AmazonEC2 ec2;

private static void init() throws Exception {
	ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
	try {
		credentialsProvider.getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
							"Cannot load the credentials from the credential profiles file. " +
							"Please make sure that your credentials file is at the correct " +
							"location (~/.aws/credentials), and is in valid format.",
							e);
			}
	ec2 = AmazonEC2ClientBuilder.standard()
			.withCredentials(credentialsProvider)
			.withRegion("us-east-1") /* check the region at AWS console */
			.build();
	}


public static void main(String[] args) throws Exception {
init();
Scanner menu = new Scanner(System.in);
int number = 0;
final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();


while(true)
{
    System.out.println(" ");
    System.out.println(" ");
    System.out.println("----------------------------------------------------------------------");
    System.out.println(" Amazon AWS Control Panel using SDK ");
    System.out.println(" ");
    System.out.println(" Cloud Computing, Computer Science Department ");
    System.out.println(" at Chungbuk National University ");
    System.out.println("----------------------------------------------------------------------");
    System.out.println(" 1. list instance			2. available zones ");
    System.out.println(" 3. start instance			4. available regions ");
    System.out.println(" 5. stop instance			6. create instance ");
    System.out.println(" 7. reboot instance			8. list images ");
    System.out.println(" 9. terminate instance			10. start monitoring instance ");
    System.out.println(" 11. stop monitoring instance		12. list keypair ");
    System.out.println(" 13. create keypair			14. delete keypair");
    System.out.println(" 15. list security group		99. quit ");
    System.out.println("----------------------------------------------------------------------");
    System.out.print("Enter an integer: ");
    number = menu.nextInt();
   
    switch(number) {
    case 1 :
        listInstances();
        break;
    case 2 :
        availableZones();
        break;
    case 3 :
        startInstances();
        break;
    case 4 :
    	availableRegions();
    	break;
    case 5 :
        stopInstances();
        break;
    case 6 :
    	createInstances();
    	break;
    case 7 :
        rebootInstances();
        break;
    case 8 :
    	listImages();
        break;
    case 9 :
    	terminateInstance();
    	break;
    case 10 :
    	startMonitoringInstance();
    	break;
    case 11 :
    	stopMonitoringInstance();
    	break;
    case 12 :
    	listKeypair();
    	break;
    case 13 :
    	createKeypair();
    	break;
    case 14 :
    	deleteKeypair();
    	break;
    case 15 :
    	listSecurityGroup();
    	break;
    case 99 :
    	quit();
    	break;
    	default :
    		quit();
        }
    }
}

// 1.
public static void listInstances() {
System.out.println("Listing instances....");
boolean done = false;

DescribeInstancesRequest describeinstancesrequest = new DescribeInstancesRequest();
while(!done) {
    DescribeInstancesResult describeinstancesresult = ec2.describeInstances(describeinstancesrequest);
    for(Reservation reservation : describeinstancesresult.getReservations()) {
        for(Instance instance : reservation.getInstances()) {
            System.out.printf(
                            "[id] %s, " +
                            "[AMI] %s, " +
                            "[type] %s, " +
                            "[state] %s, " +
                            "[monitoring state] %s",
                            instance.getInstanceId(),
                            instance.getImageId(),
                            instance.getInstanceType(),
                            instance.getState().getName(),
                            instance.getMonitoring().getState());
            }
        System.out.println();
        }
    describeinstancesrequest.setNextToken(describeinstancesresult.getNextToken());
   
    if(describeinstancesresult.getNextToken() == null) {
        done = true;
        }
    }
}

// 2.
public static void availableZones() {
	int count = 0;
    DescribeAvailabilityZonesResult describeavailabilityzonesresult = ec2.describeAvailabilityZones();

    System.out.println("Available zones....");
    for(AvailabilityZone zone : describeavailabilityzonesresult.getAvailabilityZones()) {
        System.out.printf(
        		"[id] %s, " +
                "[region] %s, " +
                "[zone] %s ",
                zone.getZoneId(),
                zone.getRegionName(),
                zone.getZoneName());
        System.out.println();
        count++;
    }
    System.out.printf("You have access to %d Availability Zones.", count);
    
}

// 3.
public static void startInstances() {
    Scanner scanner = new Scanner(System.in);
    String instance_id ="";
   
    System.out.print("Enter instance id: ");
    instance_id = scanner.next();
       
    StartInstancesRequest startinstancesrequest = new StartInstancesRequest()
                .withInstanceIds(instance_id);

        ec2.startInstances(startinstancesrequest);
        System.out.printf("Successfully started instance %s", instance_id);
}

// 4.
public static void availableRegions() {
    DescribeRegionsResult describeregionsresult = ec2.describeRegions();
    System.out.println("Available regions....");
    for(Region region : describeregionsresult.getRegions()) {
        System.out.printf(
            "[region]	%s,	" +
            "[endpoint]	%s",
            region.getRegionName(),
            region.getEndpoint());
        System.out.println();
    }
}

// 5.
public static void stopInstances() {
    Scanner scanner = new Scanner(System.in);
    String instance_id ="";
   
    System.out.print("Enter instance id: ");
    instance_id = scanner.next();

    StopInstancesRequest stopinstancesrequest = new StopInstancesRequest()
        .withInstanceIds(instance_id);

    ec2.stopInstances(stopinstancesrequest);
    System.out.printf("Successfully stopped instance %s", instance_id);
}

// 6.
public static void createInstances() {
	Scanner scanner = new Scanner(System.in);
    String ami_id ="";
    String key_name = "";
    
    System.out.print("Enter ami id: ");
    ami_id = scanner.next();
    System.out.print("Enter key name: ");
    key_name = scanner.next();

        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        RunInstancesRequest runinstancesrequest = new RunInstancesRequest()
            .withImageId(ami_id)
            .withInstanceType(InstanceType.T2Micro)
            .withMaxCount(1)
            .withMinCount(1)
            .withKeyName(key_name);
        

        RunInstancesResult runinstancesresult = ec2.runInstances(runinstancesrequest);

        String reservation_id = runinstancesresult.getReservation().getInstances().get(0).getInstanceId();
       	System.out.printf("Successfully started EC2 instance %s based on AMI %s", reservation_id, ami_id);    	
}

// 7.
public static void rebootInstances() {
    Scanner scanner = new Scanner(System.in);
    String instance_id ="";
   
    System.out.print("Enter instance id: ");
    instance_id = scanner.next();
    
    RebootInstancesRequest rebootinstancesrequest = new RebootInstancesRequest()
        .withInstanceIds(instance_id);

    RebootInstancesResult rebootinstancesresult = ec2.rebootInstances(rebootinstancesrequest);

    System.out.printf("Successfully rebooted instance %s", instance_id);
}

// 8.
public static void listImages() {
	System.out.println("Listing images....");
	DescribeImagesRequest describeimagesrequest = new DescribeImagesRequest();
	
	List<Filter> filters = new ArrayList<>();
	Filter filter = new Filter();
	filter.setName("is-public");
	
	 List<String> values = new ArrayList<>();
	 values.add("false");
	 filter.setValues(values);
	 	 
	 filters.add(filter);
	 describeimagesrequest.setFilters(filters);

	 
	 DescribeImagesResult describeimagesresult = ec2.describeImages(describeimagesrequest);	
	 for(Image images :describeimagesresult.getImages()){
			System.out.printf("[ImageID] %s, " +
	        					"[Name] %s, " +
	        					"[Owner] %s",
	        					images.getImageId(),
	        					images.getName(),
	        					images.getOwnerId());
	    }	 	
    }

// 9.
public static void terminateInstance() {
	Scanner scanner = new Scanner(System.in);
	String instance_id = "";
	
	System.out.print("Enter instance id: ");
	instance_id = scanner.next();
	
    TerminateInstancesRequest terminateInstancesRequest = new TerminateInstancesRequest()
            .withInstanceIds(instance_id);
    
    ec2.terminateInstances(terminateInstancesRequest)
            .getTerminatingInstances()
            .get(0)
            .getPreviousState()
            .getName();
    
    System.out.println("The Instance is terminated with id: "+ instance_id);
}

// 10.
public static void startMonitoringInstance() {
	 Scanner scanner = new Scanner(System.in);
	    String instance_id ="";
	   
	    System.out.print("Enter instance id: ");
	    instance_id = scanner.next();
	    
	MonitorInstancesRequest monitorinstancesrequest = new MonitorInstancesRequest()
	        .withInstanceIds(instance_id);

	ec2.monitorInstances(monitorinstancesrequest);
	System.out.printf("Successfully enabled monitoring for instance %s", instance_id);
}

// 11.
public static void stopMonitoringInstance() {
	Scanner scanner = new Scanner(System.in);
    String instance_id ="";
   
    System.out.print("Enter instance id: ");
    instance_id = scanner.next();
    
	UnmonitorInstancesRequest unmonitorinstancesrequest = new UnmonitorInstancesRequest()
		    .withInstanceIds(instance_id);

		ec2.unmonitorInstances(unmonitorinstancesrequest);
		System.out.printf("Successfully disabled monitoring for instance %s", instance_id);
}

// 12.
public static void listKeypair() {
    DescribeKeyPairsResult describekeypairsresult = ec2.describeKeyPairs();

    for(KeyPairInfo key_pair : describekeypairsresult.getKeyPairs()) {
        System.out.printf(
            "[Keyname] %s	" +
            "[fingerprint] %s \n",
            key_pair.getKeyName(),
            key_pair.getKeyFingerprint());
    }
}

// 13.
public static void createKeypair() {
	Scanner scanner = new Scanner(System.in);
	String key_id ="";
	String key_name = "";
	
	System.out.print("Enter key name: ");
    key_name = scanner.next();

    CreateKeyPairRequest createkeypairrequest = new CreateKeyPairRequest()
        .withKeyName(key_name);

    CreateKeyPairResult createkeypairresult = ec2.createKeyPair(createkeypairrequest);

    System.out.printf("Successfully created key pair named %s", key_name);
}

// 14.
public static void deleteKeypair() {
	Scanner scanner = new Scanner(System.in);
	String key_id ="";
	String key_name = "";
	
	System.out.print("Enter key name: ");
    key_name = scanner.next();

     DeleteKeyPairRequest deletekeypairrequest = new DeleteKeyPairRequest()
         .withKeyName(key_name);

     DeleteKeyPairResult deletekeypairresult = ec2.deleteKeyPair(deletekeypairrequest);

     System.out.printf("Successfully deleted key pair named %s", key_name);
}

// 15.
public static void listSecurityGroup() {
	String group_id = "";

    DescribeSecurityGroupsRequest describesecuritygroupsrequest = new DescribeSecurityGroupsRequest()
            .withGroupIds(group_id);

    DescribeSecurityGroupsResult describesecuritygroupsresult = ec2.describeSecurityGroups(describesecuritygroupsrequest);

    for(SecurityGroup group : describesecuritygroupsresult.getSecurityGroups()) {
        System.out.printf(
            "[id] %s	" +
            "[vpc id] %s	" +
            "[description] %s	" +
            "[name] %s \n",
            group.getGroupId(),
            group.getVpcId(),
            group.getDescription(),
            group.getGroupName());
    }
}

// 99.
public static void quit() {
	System.out.println("exit...");
	System.exit(0);
}

} 
