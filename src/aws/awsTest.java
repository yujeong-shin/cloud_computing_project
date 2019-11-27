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
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.CreateTagsResult;
import com.amazonaws.services.ec2.model.DeleteKeyPairRequest;
import com.amazonaws.services.ec2.model.DeleteKeyPairResult;
import com.amazonaws.services.ec2.model.DeleteSecurityGroupRequest;
import com.amazonaws.services.ec2.model.DeleteSecurityGroupResult;
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
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesResult;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
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
int number = 0; final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();


while(true)
{
    System.out.println(" ");
    System.out.println(" ");
    System.out.println("------------------------------------------------------------");
    System.out.println(" Amazon AWS Control Panel using SDK ");
    System.out.println(" ");
    System.out.println(" Cloud Computing, Computer Science Department ");
    System.out.println(" at Chungbuk National University ");
    System.out.println("------------------------------------------------------------");
    System.out.println(" 1. list instance 2. available zones ");
    System.out.println(" 3. start instance 4. available regions ");
    System.out.println(" 5. stop instance 6. create instance ");
    System.out.println(" 7. reboot instance 8. list images ");
    System.out.println(" 9. list keypair 10. create keypair");
    System.out.println(" 11. delete keypair 12. list security group ");
    System.out.println(" 13. delete security group 99. quit");
    System.out.println("------------------------------------------------------------");
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
    	listKeypair();
    	break;
    case 10 :
    	createKeypair();
    	break;
    case 11 :
    	deleteKeypair();
    	break;
    case 12 :
    	listSecurityGroup();
    	break;
    case 13 :
    	deleteSecurityGroup();
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

DescribeInstancesRequest DI_request = new DescribeInstancesRequest();
while(!done) {
    DescribeInstancesResult DI_response = ec2.describeInstances(DI_request);
    for(Reservation reservation : DI_response.getReservations()) {
        for(Instance instance : reservation.getInstances()) {
            System.out.printf(
                            "[id] %s, " +
                            "[AMI] %s, " +
                            "[type] %s, " +
                            "[state] %10s, " +
                            "[monitoring state] %s",
                            instance.getInstanceId(),
                            instance.getImageId(),
                            instance.getInstanceType(),
                            instance.getState().getName(),
                            instance.getMonitoring().getState());
            }
        System.out.println();
        }
    DI_request.setNextToken(DI_response.getNextToken());
   
    if(DI_response.getNextToken() == null) {
        done = true;
        }
    }
}

// 2.
public static void availableZones() {
	int count = 0;
	
    //final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    DescribeAvailabilityZonesResult DAZ_response = ec2.describeAvailabilityZones();

    System.out.println("Available zones....");
    for(AvailabilityZone zone : DAZ_response.getAvailabilityZones()) {
        System.out.printf(
        		"[id] %s, " +
                "[region]   %s, " +
                "[zone]     %s ",
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
       
    StartInstancesRequest SI_request = new StartInstancesRequest()
                .withInstanceIds(instance_id);

        ec2.startInstances(SI_request);

        System.out.printf("Successfully started instance %s", instance_id);
}

// 4.
public static void availableRegions() {
	//final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    DescribeRegionsResult DR_response = ec2.describeRegions();
    System.out.println("Available regions....");
    for(Region region : DR_response.getRegions()) {
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
   
    final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    StopInstancesRequest SI_request = new StopInstancesRequest()
        .withInstanceIds(instance_id);

    ec2.stopInstances(SI_request);
}

// 6.
public static void createInstances() {
	Scanner scanner = new Scanner(System.in);
    String ami_id ="";
    //String name = "java-test";
    
    System.out.print("Enter ami id: ");
    ami_id = scanner.next();

        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        RunInstancesRequest run_request = new RunInstancesRequest()
            .withImageId(ami_id)
            .withInstanceType(InstanceType.T2Micro)
            .withMaxCount(1)
            .withMinCount(1)
            .withKeyName("cloud-test");
        

        RunInstancesResult run_response = ec2.runInstances(run_request);

        String reservation_id = run_response.getReservation().getInstances().get(0).getInstanceId();

        /*
        Tag tag = new Tag()
            .withKey("Name")
            .withValue(name);

        CreateTagsRequest tag_request = new CreateTagsRequest()
            .withTags(tag);

        CreateTagsResult tag_response = ec2.createTags(tag_request);

        */
        	System.out.printf(
            "Successfully started EC2 instance %s based on AMI %s",
            reservation_id, ami_id);
    	
}

// 7.
public static void rebootInstances() {
    Scanner scanner = new Scanner(System.in);
    String instance_id ="";
   
    System.out.print("Enter instance id: ");
    instance_id = scanner.next();
   
    //final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    RebootInstancesRequest RI_request = new RebootInstancesRequest()
        .withInstanceIds(instance_id);

    RebootInstancesResult response = ec2.rebootInstances(RI_request);

    System.out.printf("Successfully rebooted instance %s", instance_id);
}

// 8.
public static void listImages() {
	//final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
	System.out.println("Listing images....");
	DescribeImagesRequest describeImagesRequest = new DescribeImagesRequest();
	
	List<Filter> filters = new ArrayList<>();
	Filter filter = new Filter();
	filter.setName("is-public");

	
	 List<String> values = new ArrayList<>();
	 values.add("false");
	 filter.setValues(values);
	 	 
	 filters.add(filter);
	 
	 describeImagesRequest.setFilters(filters);

	 
	 DescribeImagesResult describeImagesResult = ec2.describeImages(describeImagesRequest);	
	 for(Image images :describeImagesResult.getImages()){
			System.out.printf("[ImageID] %s,	" +
	        					"[Name] %s,	" +
	        					"[Owner] %s",
	        					images.getImageId(),
	        					images.getName(),
	        					images.getOwnerId());
	    }	 	
    }

// 9.
public static void listKeypair() {
	final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    DescribeKeyPairsResult response = ec2.describeKeyPairs();

    for(KeyPairInfo key_pair : response.getKeyPairs()) {
        System.out.printf(
            "Found key pair with name %s " +
            "and fingerprint %s \n",
            key_pair.getKeyName(),
            key_pair.getKeyFingerprint());
    }
}

// 10.
public static void createKeypair() {
	Scanner scanner = new Scanner(System.in);
	String key_id ="";
	String key_name = "";
	
	System.out.print("Enter key name: ");
    key_name = scanner.next();
    
    final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    CreateKeyPairRequest request = new CreateKeyPairRequest()
        .withKeyName(key_name);

    CreateKeyPairResult response = ec2.createKeyPair(request);

    System.out.printf("Successfully created key pair named %s", key_name);
}

// 11.
public static void deleteKeypair() {
	Scanner scanner = new Scanner(System.in);
	String key_id ="";
	String key_name = "";
	
	System.out.print("Enter key name: ");
    key_name = scanner.next();

     final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

     DeleteKeyPairRequest request = new DeleteKeyPairRequest()
         .withKeyName(key_name);

     DeleteKeyPairResult response = ec2.deleteKeyPair(request);

     System.out.printf(
         "Successfully deleted key pair named %s", key_name);
}

// 12.
public static void listSecurityGroup() {
	String group_id = "";

    final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    DescribeSecurityGroupsRequest request =
        new DescribeSecurityGroupsRequest()
            .withGroupIds(group_id);

    DescribeSecurityGroupsResult response =
        ec2.describeSecurityGroups(request);

    for(SecurityGroup group : response.getSecurityGroups()) {
        System.out.printf(
            "Found security group with id %s, " +
            "vpc id %s " +
            "and description %s \n",
            group.getGroupId(),
            group.getVpcId(),
            group.getDescription());
    }
}

// 13.
public static void deleteSecurityGroup() {
	Scanner scanner = new Scanner(System.in);
	String group_name = "";
	
	System.out.print("Enter securitygroup name: ");
	group_name = scanner.next();

    final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

    DeleteSecurityGroupRequest request = new DeleteSecurityGroupRequest()
        .withGroupId(group_name);

    DeleteSecurityGroupResult response = ec2.deleteSecurityGroup(request);

    System.out.printf(
        "Successfully deleted security group with id %s", group_name);
}
 
//99.
public static void quit() {
	System.out.println("exit...");
	System.exit(0);
}


} 
