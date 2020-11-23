package com.amazonaws.samples;

import java.util.Scanner;
import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.AvailabilityZone;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeImagesRequest;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.Response;
import com.amazonaws.services.codepipeline.model.AWSSessionCredentials;
import com.amazonaws.services.ec2.model.DescribeImageAttributeRequest;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.CreateTagsResult;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ecr.model.DescribeImagesFilter;
import com.amazonaws.services.simplesystemsmanagement.model.transform.DescribeAvailablePatchesRequestMarshaller;
import com.amazonaws.services.ec2.model.DryRunResult;
import com.amazonaws.services.ec2.model.DryRunSupportedRequest;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceType;
import com.amazonaws.services.ec2.model.RebootInstancesRequest;
import com.amazonaws.services.ec2.model.RebootInstancesResult;
import com.amazonaws.services.ec2.model.Region;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeRegionsResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;

public class Awsinstance
{
		private static final String RunInstancesRequest = null;
		/*
		* Cloud Computing, Data Computing Laboratory
		* Department of Computer Science
		* Chungbuk National University
		*/
		static AmazonEC2 ec2;
		
		private static void init() throws Exception
		{
			/*
			* The ProfileCredentialsProvider will return your [default]
			* credential profile by reading from the credentials file located at
			* (~/.aws/credentials).
			*/
			ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
			try 
			{
				credentialsProvider.getCredentials();
			}
			catch (Exception e)
			{
				throw new AmazonClientException(
							"Cannot load the credentials from the credential profiles file. " +
							"Please make sure that your credentials file is at the correct " +
							"location (~/.aws/credentials), and is in valid format.",
							e);
			}
			ec2 = AmazonEC2ClientBuilder.standard()
					.withCredentials(credentialsProvider)
					.withRegion("us-east-1") /* 예제코드에선 2번이었지만 1로 수정 */
					.build();
		}

	public static void main(String[] args) throws Exception
	{
		init();
		Scanner menu = new Scanner(System.in);
		Scanner id_string = new Scanner(System.in);
		int number = 0;
		String name;
		String name2;
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
			System.out.println(" 1. list instance   |  2. available zones ");
			System.out.println(" 3. start instance  |  4. available regions ");
			System.out.println(" 5. stop instance   |  6. create instance ");
			System.out.println(" 7. reboot instance |  8. list images ");
			System.out.println(" 9. delete instance |  10. find running ");  //내가 추가 구현하려는 부분
			System.out.println(" 99. quit ");
			System.out.println("------------------------------------------------------------");
			
			System.out.print("Enter an integer: ");
			number = menu.nextInt();

			switch(number)
			{
				case 1:
					listInstances();
					break;
				case 2:
					AvailableZones();
					break;
				case 3:
					System.out.print("Enter instance id : ");
					name = id_string.nextLine();
					StartInstasnce(name); 
					break;
				case 4:
					AvailableRegions();
					break;
				case 5:
					System.out.print("Enter instance id : ");
					name = id_string.nextLine();
					StopInstance(name);
					break;
				case 6:
					System.out.print("Enter Instance name : ");
					name = id_string.nextLine();
					System.out.print("Enter AMi id : ");
					name2 = id_string.nextLine();
					CreateInstance(name, name2);
					break;
				case 7:
					System.out.print("Enter instance id : ");
					name = id_string.nextLine();
					RebootInstance(name);
					break;
				case 8:
					ListImages();
					break;
				case 9:
					System.out.print("Enter instance id : ");
					name = id_string.nextLine();
					deleteinstance(name);
					break;
				case 10:
					findrunning();
					break;
				case 99:
					System.out.println("The program has been terminated successfully!");
					menu.close();
					id_string.close();
					System.exit(0);
					break;	
			}	
		}
	}

	public static void listInstances()
	{
			System.out.println("Listing instances....");
			boolean done = false;
			
			DescribeInstancesRequest request = new DescribeInstancesRequest();
			
			while(!done)
			{
				com.amazonaws.services.ec2.model.DescribeInstancesResult response = ec2.describeInstances(request);
					for(Reservation reservation : response.getReservations())
					{
						for(com.amazonaws.services.ec2.model.Instance instance : reservation.getInstances())
						{
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
					
					request.setNextToken(response.getNextToken());
		
				if(response.getNextToken() == null)
				{
					done = true;
				}
			}
		}	
//제공된 코드
	
	public static void AvailableZones()
	{
		int counter = 0;
		DescribeAvailabilityZonesResult zones_response = ec2.describeAvailabilityZones();
		System.out.println("Available zones....");
		
			for(AvailabilityZone zone : zones_response.getAvailabilityZones()) {
			    System.out.printf(
			    		"[id] %s " +
			        "[status] %s " +
			        "[region] %s\n",
			        zone.getZoneName(),
			        zone.getState(),
			        zone.getRegionName());
			    counter++;
			}
		System.out.print("You have access to " + counter + " Availability Zones");
	}
//완료 *
	
	public static void StartInstasnce(String instance_id)
	{
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
        
        StartInstancesRequest request = new StartInstancesRequest().withInstanceIds(instance_id);

        ec2.startInstances(request);

        System.out.printf("Successfully started instance %s", instance_id);
		
	}
//완료 *
	
	public static void AvailableRegions()
	{
		DescribeRegionsResult regions_response = ec2.describeRegions();
		System.out.println("Available regions....");
		for(Region region : regions_response.getRegions())
		{
		    System.out.printf(
		        "[region] %s " +
		        "[endpoint] %s\n",
		        region.getRegionName(),
		        region.getEndpoint());
		}
	}
//완료 *
	
	public static void StopInstance(String instance_id)
	{
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		StopInstancesRequest request = new StopInstancesRequest().withInstanceIds(instance_id);

	    ec2.stopInstances(request);

	    System.out.printf("Successfully stop instance %s", instance_id);
	}
//완료 *
	
	public static void CreateInstance(String instance_name, String ami_id)
	{
		 final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

	        RunInstancesRequest run_request = new RunInstancesRequest()
	            .withImageId(ami_id)
	            .withInstanceType(InstanceType.T2Micro)
	            .withMaxCount(1)
	            .withMinCount(1);

	        RunInstancesResult run_response = ec2.runInstances(run_request);

	        String reservation_id = run_response.getReservation().getInstances().get(0).getInstanceId();

	        Tag tag = new Tag().withKey("Name").withValue(instance_name);

	        CreateTagsRequest tag_request = new CreateTagsRequest().withResources(reservation_id).withTags(tag);
	        ec2.createTags(tag_request);
	        System.out.printf("Successfully started EC2 instance %s based on AMI %s",reservation_id, ami_id);
	}
//완료 *
	
	public static void RebootInstance(String instance_id)
	{
        final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();

        RebootInstancesRequest request = new RebootInstancesRequest().withInstanceIds(instance_id);

       ec2.rebootInstances(request);

        System.out.printf("Successfully rebooted instance %s", instance_id);	
	}
//완료 (이긴한데 스탑되어있는 인스턴스를 리부트시킬때 이상...하다? AWS에서 exception 뜨는 걸 보니 정책적으로 안되는듯? 함)
	
	public static void ListImages()
	{
		System.out.println("Listing images....");
		
		Filter filter = new Filter().withName("is-public").withValues("false"); 
		// private 이미지가 아니면 이미지 정보가 리턴되지 않는 문제 filter 객체 사용을 통한 해결
		DescribeImagesRequest request = new DescribeImagesRequest().withFilters(filter);
		DescribeImagesResult respose = ec2.describeImages(request);
		
			for(Image image : respose.getImages())
			{
					System.out.printf(
					"[image ID] %s, " +
					"[name] %s, " +
					"[Owner] %s, ",
					image.getImageId(),
					image.getName(),
					image.getOwnerId());
			}
				System.out.println();
	}
//완료 *

/*
 *  추가기능: 현재 가용 가능한(in running) 가상머신만 출력하는 기능 추가 11/22
 *  Ec2 라이브러리와 기본으로 제공된 list 코드를 참고하여 추가 구현
 */	
	public static void findrunning()
	{
		 AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		 System.out.println("Finding Running instances....");
	        try
	        {
	            Filter filter = new Filter("instance-state-name");
	            filter.withValues("running");

	            DescribeInstancesRequest request = new DescribeInstancesRequest();
	            request.withFilters(filter);

	            DescribeInstancesResult response = ec2.describeInstances(request);

	            for (Reservation reservation : response.getReservations())
	            {
	                for (Instance instance : reservation.getInstances())
	                {
	                    System.out.printf(
	                    				"[id] %s, " +
	                            		"[AMI] %s, " +
	                                    "[type] %s, " +
	                                    "[state] %s " +
	                                    "[monitoring state] %s\n",
	                            instance.getInstanceId(),
	                            instance.getImageId(),
	                            instance.getInstanceType(),
	                            instance.getState().getName(),
	                            instance.getMonitoring().getState());
	                }
	            }
	            System.out.print("\nDone");

	        } catch (SdkClientException e)
	        {
	            e.getStackTrace();
	        }
	}
//완료 *	
	
/*
 *  추가기능: 인스턴스 stop이 아닌 terminate기능 추가 11/22
 *  ec2라이브러리를 찾아보고 start/stop instance의 구현을 응용하여 구현
 */
	public static void deleteinstance(String instance_id)
	{
		final AmazonEC2 ec2 = AmazonEC2ClientBuilder.defaultClient();
		
		TerminateInstancesRequest request = new TerminateInstancesRequest().withInstanceIds(instance_id);
		ec2.terminateInstances(request);
		 
		System.out.printf("Successfully terminated instance %s", instance_id);	
	}
//완료 *
	
}



