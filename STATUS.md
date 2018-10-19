# Subject
https://docs.google.com/document/d/1VVrKrDH9Hj0iGAbUADNwkXJDBd2Hnzye52RD3ASlBMA/edit

# Team
- HAJJI Amine
- TOUTAIN Xavier
- BENZA Amandine
- FORNALI Damien

> Master II - SI5<br>
> University / Polytech Sophia-Nice<br>
> Cloud Computing - 2018

# Delivery 1 - 19/10

1. Architecture
	- To simplify, our upload/download files formats will be standard like .txt or even images (.png) according to the time we have. 
	- We will use a database offered by the Google Cloud Storage (NoSQL or RDBMS). When compared to relational databases, NoSQL databases are more flexible and scalable. These are attributes that interest us for this project.
	- For the architecture overview, please refer to the architecture folder which contains our different schemas. 
	It contains our architectures for the different user grades.

2. Elasticity
	- As a start, we chose to use only one instance in order to establish our lower bound.
	- A horizontal elasticity is interesting but not only comes with advantages. Indeed using it can cause problems.
	- instances problem > take times
	- No wanted for now, focus on one 24/7 available instance

3. Plateform cost

 We tried to estimate the amount such a platform would cost by month.<br>
 As we have no scaling to refer to, we issue difficulty to evaluate how many instances we need for our users capacity of 1000 users. Thus, we choose to start with only one instance, in order to be able to evaluate our lower bound cost.<br>

 The Google price calculation takes in account three components:<br>
> - The Compute Engine cost<br>
For the Compute Engine, we tried to estimate the lower bound, but again, we have no examples or references to really know what we are doing.   
> - The App Engine cost<br>
> - The Cloud Storage cost<br>

		[Compute engine cost estimation recap]
		730 total hours per month
		VM class: regular
		Instance type: n1-standard-2
		Region: Belgium
		Sustained Use Discount: 30% 
		Effective Hourly Rate: EUR 0.062
		Estimated Component Cost: EUR 45.56 per 1 month
		EUR: Total Estimated Cost: EUR 45.56 per 1 month
		USD: Total Estimated Cost: USD 53.45 per 1 month 

		----

		- [App engine]

		----

		- [Cloud Storage] (Coming soon...)

# Subject resume and notes
Noob [0-100]:<br>
We will use a push queue to implement this grade.<br>
The Noob' tasks are handled consecutively.<br>
He is able to send only one operation by minute. If he tries to send more he will get a mail which title/content will be "lol non noob".<br>
This grade users' files are deleted after 5 minutes.<br>

Casual [101-200]:<br>
We will use a push queue to implement this grade.<br>
The system offers to the Casual 2 operations in parallel by minute.<br>
Casual files are deleted after 10 minutes.<br>

Leet [> 201]:<br>
The system offers to the Leet 4 operations in parallel (1 min delay).<br>
Its files are stored for only 30 minutes long.<br>

https://cloud.google.com/appengine/docs/standard/java/taskqueue/push/
Push queue:
	- put task into queue send task to worker
	- worker a cloud processus

https://cloud.google.com/appengine/docs/standard/java/taskqueue/pull/
Pull queue:
	- worker gets the tasks

# Questions
- Does the 1 minute delay timer starts at the start or the end of the upload ?
- What to refer to, to establish costs by user grade ?

# Links
Intro: https://fhermeni.github.io/sacc/sacc-introduction.pdf<br>
sacc-architecture: https://fhermeni.github.io/sacc/sacc-architecture.pdf<br>
Console google cloud: https://console.cloud.google.com/<br>
Course page: https://fhermeni.github.io/sacc/<br>
Postman: https://www.getpostman.com/<br>
Push queue: https://cloud.google.com/appengine/docs/standard/java/taskqueue/push/<br>
Pull queue: https://cloud.google.com/appengine/docs/standard/java/taskqueue/pull/<br>
Export billing data to a file: https://cloud.google.com/billing/docs/how-to/export-data-file?rd=2&visit_id=636754899995029268-3782129590<br>
Mail API overview: https://cloud.google.com/appengine/docs/standard/java/mail/<br>
Sending email from an instance: https://cloud.google.com/compute/docs/tutorials/sending-mail/<br>
Platform pricing calculator: https://cloud.google.com/products/calculator/<br>
Compute engine pricing: https://cloud.google.com/compute/pricing<br>
Google Machine Types: https://cloud.google.com/compute/docs/machine-types<br>
Google cloud storages: https://cloud.google.com/products/storage/<br>
Price calculator: https://cloud.google.com/products/calculator/<br>