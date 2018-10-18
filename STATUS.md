# Team
HAJJI Amine
TOUTAIN Xavier
BENZA Amandine
FORNALI Damien

    Cloud Computing - 2018

# Subject
https://docs.google.com/document/d/1VVrKrDH9Hj0iGAbUADNwkXJDBd2Hnzye52RD3ASlBMA/edit

# Delivery 1 - 19/10
Noob [0-100]:
	- push queue
	- tasks handled consecutively
	- one by user
	- > 1 request by user by minute,
		the user get a mail "lol non noob"
	- files deleted after 5 minutes

Casual [101-200](upload/download):
	- pull queue
	- 2 ops in parallel by user (by minute)
	- files deleted after 10 minutes

Leet [> 201]:
	- 4 op by user in parallel (1 min delay)
	- files stored for 30 minutes

https://cloud.google.com/appengine/docs/standard/java/taskqueue/push/
Push queue:
	- put task into queue send task to worker
	- worker a cloud processus

https://cloud.google.com/appengine/docs/standard/java/taskqueue/pull/
Pull queue:
	- worker gets the tasks

- Overall architecture and precise description of the use of queues.
- Use (or not) of elasticity.
- Cost of the platform (basic, per user) without storage.

1. Architecture
	- text files or even images
	- Database (proposed by google ?) noSQL
	- https://www.lucidchart.com/documents/edit/60177311-378e-4c72-a8da-d42296f08eff/0?shared=true&

2. Elasticity
	- horizontal elasticity ?
	- instances problem > take times
	- No wanted for now, focus on one 24/7 available instance

3. Plateform cost
 > Start of billing
	- multiple components:
		- compute engine
			- 1 instance
			- Free OS: Ubuntu...
			- VM Class: Regular
			- Instance type: n1-standard-2
				> (2 cpu, 7.5GB): seems reasonable for our objectives
			n1-standard-2	2	7.5GB	$48.5500	$14.60

			- Local SSD: no for now
			- Datacenter location: Belgium
			- Average hours per day each server is running: 24/7

					<RECAP>
				730 total hours per month
			VM class: regular
			Instance type: n1-standard-2
			Region: Belgium
			Sustained Use Discount: 30% 
			Effective Hourly Rate: EUR 0.062
			Estimated Component Cost: EUR 45.56 per 1 month
			EUR: Total Estimated Cost: EUR 45.56 per 1 month
			USD: Total Estimated Cost: USD 53.45 per 1 month 

			https://cloud.google.com/products/calculator/#id=057d94d7-78b0-4fbd-896c-3be2b6bedd5c

			- app engine

			- cloud storage TODO later


# Questions
- 1 minute delay timer starts end of upload ?
- 

# Links
Intro: https://fhermeni.github.io/sacc/sacc-introduction.pdf
sacc-architecture: https://fhermeni.github.io/sacc/sacc-architecture.pdf
Console google cloud: https://console.cloud.google.com/
Course page: https://fhermeni.github.io/sacc/
Postman: https://www.getpostman.com/
Push queue: https://cloud.google.com/appengine/docs/standard/java/taskqueue/push/
Pull queue: https://cloud.google.com/appengine/docs/standard/java/taskqueue/pull/
Export billing data to a file: https://cloud.google.com/billing/docs/how-to/export-data-file?rd=2&visit_id=636754899995029268-3782129590
Mail API overview: https://cloud.google.com/appengine/docs/standard/java/mail/
Sending email from an instance: https://cloud.google.com/compute/docs/tutorials/sending-mail/
Platform pricing calculator: https://cloud.google.com/products/calculator/
Compute engine pricing: https://cloud.google.com/compute/pricing
Google Machine Types: https://cloud.google.com/compute/docs/machine-types