#JOB-GUIDE

## Inspiration
We observed a lot of students who are trying to enter the corporate world, struggling to search for jobs online as there are many job portals. As a student you often find yourself lost in job information from different websites. And we started to wonder if all the jobs from the different websites based on my requirements/filters are available at one place where I can quickly navigate and apply to the desired job easily.

This inspired us to make **JOB GUIDE** a tool to help you in getting closer to your dream job.

## What it does
This solves the problem of the majority of Indian graduates who are trying to enter the corporate world. By using **JOB GUIDE** you can streamline the process of Job search by making it much more efficient and time-saving. **JOB GUIDE** emphasizes more on customizability so that we can find jobs that are tailored for you.

**JOB GUIDE** helps you to find internships as well.

**JOB GUIDE**'s assistant gathers all the job openings based on your preferences from different websites, simplifies, summarizes, and automates the process of manual job search, and sends the information into your mail inbox so that you can be choosy for your dream job. 

## How we built it
We built the front-end of the website using HTML, CSS, JavaScript, and JQUERY, for web scraping we used Jsoup a java library designed to parse, extract and manipulate the job data provided by different websites, and used javax-mail API to send the simplified and summarized job offers data to the user.
We deployed MongoDB for storing and retrieving users' data and preferences, using mongoose framework for interacting with the database we automated all the processes by writing business logic in the server using express js a backend framework for NodeJS. Also, we used bcrypt a password hashing function to secure passwords in the database.

## Challenges we ran into
This was our first time automating a real task that has the potential of solving a real-life problem, we experienced many issues while performing CRUD operations involving real-time data. Also faced difficulties while manipulating the job data from the website to present job data in simplified form to the user to make their experience with **JOB GUIDE** user-friendly.

## Accomplishments that we're proud of
We are proud to have developed something that can help millions of young Indian graduates to overcome the difficulty of entering the corporate world.
As we have limited practical skillset in building full stack web applications we are satisfied with how the minimum viable product turned out to be.

## What we learned
- We learned how to implement complex business logic in the server.
- Got experience in building applications that do CRUD operations.
- Learned how to integrate front end with backend.

## What's next for JOB GUIDE - ALL IN ONE SOLUTION FOR YOUR CAREER.

- We plan to further expand by adding support for various other job portals to our service.
- We plan to integrate Machine Learning to improve the efficiency of scraping various other job portals and also give the user a more personalized experience.

