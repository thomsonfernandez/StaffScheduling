

# How To Run The Project


### Unzip the project zip file to a location - Eg : C:\Thomson\SpringBoot\staff-scheduling
Open command prompt and go to the location : C:\Thomson\SpringBoot\staff-scheduling

	1.Run "docker-compose up" below command 
			C:\Thomson\SpringBoot\staff-scheduling>docker-compose up

	2. Open another command prompt and run "docker ps"
			C:\Thomson\SpringBoot\staff-scheduling>docker ps
			
	CONTAINER ID   IMAGE              COMMAND                  CREATED       STATUS              PORTS                    NAMES
	92848fc0eece   staff-scheduling   "java -jar staff-sch…"   2 hours ago   Up 58 seconds       0.0.0.0:8080->8080/tcp   staff-scheduling_server_1
	183391c17ecb   mysql:5.7          "docker-entrypoint.s…"   2 hours ago   Up About a minute   3306/tcp, 33060/tcp      staff-scheduling_mysqldb_1

	3. Execute the command "docker exec -it <containerId> bash" - This is for taking the mysql command line prompt
			C:\Thomson\SpringBoot\staff-scheduling>docker exec -it 183391c17ecb bash
			
	4. Login to mysql with username and password
			root@183391c17ecb:/# mysql -uadmin -proot
			
	5. Test to check databases
			show databases;		
			
	6. Run the below queries in same order.
			INSERT INTO staffscheduling.roles VALUES(1,'ROLE_ADMIN');
			INSERT INTO staffscheduling.roles VALUES(2,'ROLE_STAFF');
			insert into staffscheduling.users values(1, 'admin@gmail.com', 'admin' , '$2a$10$kxKCgBmtU0Z2jdO80QVUI.HlgtHf2hGoIs9fl48rR8Yb51agxd3Tq', 'admin');
			insert into staffscheduling.user_roles values (1,1);
			
Now you are good to go with testing using POSTMAN or any other tools


URLs for test:



			1. 		GET users : 						http://localhost:8080/users
			2. 		GET user by id : 				http://localhost:8080/users/2
			3. 		GET schedules:					http://localhost:8080/schedules
			4. 		GET schedules by id: 			http://localhost:8080/schedules/1
			5. 		POST Register user: 			http://localhost:8080/api/auth/signup
			6. 		POST Login user : 				http://localhost:8080/api/auth/signin
			7. 		DELETE user by user name:		http://localhost:8080/users/delete/username/johaan
			8. 		POST Create schedule:			http://localhost:8080/schedules
			9. 		GET schedules for user:		http://localhost:8080/users/3/schedules
			10. 	DELETE user by id : 			http://localhost:8080/users/2
			11. 	GET schedules in date range:	http://localhost:8080/schedules/2021-11-22/2022-06-20
			12.		GET user schedule date range: http://localhost:8080/users/3/schedules/2021-11-22/2022-06-20
			13. 	DELETE schedules for user : 	http://localhost:8080/users/3/schedules
			14. 	PUT edit user : 					http://localhost:8080/users/edit
			15. 	GET sign out user : 			http://localhost:8080/api/auth/signout
			16. 	PUT edit schedule : 			http://localhost:8080/schedules/edit/19
			
			
			
	
	
	
#OPEN API SPEC : http://localhost:8080/swagger-ui.html
