import com.freshersworld.Freshersworld;
import com.indeed.Indeed;
import com.internshala.Internshala;
import com.linkedin.LinkedIn;
import com.utils.clientemail.ClientEmail;


import java.io.File;



public class Main {
    //    args : 1. role, 2. location 3.type 4. salary 5. name 6. e-mail
    public static void main(String[] args) {
        String html = """
                
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Document</title>
                    <style>
                        @import url('https://fonts.googleapis.com/css2?family=Raleway:wght@500&display=swap');
                        body {
                            font-family: 'Raleway', sans-serif;
                        }
                    </style>
                </head>
                <body>
                    <h1>Hi """ + " " +args[args.length-2] + "," +
                    """
                    </h1>
                    <h3>Here's your curated job offers collection
                        provided by JOB GUIDE.
                    </h3>
                    <h4>Please find the attached file you requested.</h4>
                    </body>
                    </html>
                """;
        Internshala internshala = null;
        Indeed indeed = null;
        LinkedIn linkedIn = null;
        Freshersworld freshersworld = null;
        if(args[2].equals("JOB")) {
            internshala = new Internshala("https://internshala.com/jobs/"+ args[0].split(" ")[0] + "-" + args[0].split(" ")[1] + "-jobs-in-"+args[1]+"/3lpa-job");
            indeed = new Indeed("https://in.indeed.com/jobs?q="+args[0].split(" ")[0]+"%20"+args[0].split(" ")[1]+ "%20" + "&l="+args[1],"JOB");
            linkedIn = new LinkedIn("https://in.linkedin.com/jobs/search?keywords="+args[0].split(" ")[0]+"%20"+args[0].split(" ")[1]+"&location="+args[1],"JOB");
            freshersworld = new Freshersworld("https://www.freshersworld.com/jobs/jobsearch/"+args[0].split(" ")[0]+"-" + args[0].split(" ")[1] + "-jobs-in-"+ args[1],"JOB");
        }
        else{
            internshala = new Internshala("https://internshala.com/internships/"+ args[0].split(" ")[0] + "-" + args[0].split(" ")[1] +"-internship-in-"+args[1]);
            indeed = new Indeed("https://in.indeed.com/jobs?q="+args[0].split(" ")[0]+"%20"+args[0].split(" ")[1]+ "%20" +args[0].split(" ")[2] +"%20"+ "&l="+args[1],"INTERN");
            linkedIn = new LinkedIn("https://in.linkedin.com/jobs/search?keywords="+args[0].split(" ")[0]+"%20"+args[0].split(" ")[1]+"%20"+ args[0].split(" ")[2] +"&location="+args[1],"INTERN");
            freshersworld = new Freshersworld("https://www.freshersworld.com/jobs/jobsearch/"+args[0].split(" ")[0]+"-" + args[0].split(" ")[1]+ "-" + args[0].split(" ")[2] + "-jobs-in-"+ args[1] ,"INTERN");
        }
        Thread t1 = new Thread(internshala);
        Thread t2 = new Thread(indeed);
        Thread t3 = new Thread(linkedIn);
        Thread t4 = new Thread(freshersworld);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try{
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        File file = new File("jobs.csv");
        ClientEmail ce = new ClientEmail("YOUR_EMAIL","YOUR_PASSWORD"
                ,args[args.length-1],"Get ready to kickstart your career with JOBGUIDE!",html,file);

        ce.sendMail();
        file.deleteOnExit();
    }
}
