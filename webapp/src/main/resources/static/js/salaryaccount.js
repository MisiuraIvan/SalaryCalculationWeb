function salaryAccount(){
    alert("dfgd");
    let timenorm,oklad,worktime,sickleave,holiday,overtime,award;
    timenorm=document.getElementById("timenorm").value;
    oklad=document.getElementById("oklad").value;
    worktime=document.getElementById("workHours").value;
    sickleave=document.getElementById("sickleave").value;
    holiday=document.getElementById("holiday").value;
    overtime=document.getElementById("overtime").value;
    award=document.getElementById("award").value;
    let proc=1+award*0.01;

    let swork = oklad * worktime*proc/timenorm;

    /*let avgsalary=0;*/

    let ssick;
    if(sickleave>40) {
        ssick = 0.8 * oklad*40/timenorm;
        ssick+=(sickleave-40) * oklad/timenorm;
    }else{
        ssick = sickleave* 0.8 * oklad/timenorm;
    }
    /*try {
        Connection.writer.writeUTF("dataforavgsalary "+listid.getValue()+" "+date.getValue());
        Connection.writer.flush();
        String response = Connection.reader.readUTF();
        avgsalary= parseDouble(response);
        System.out.println(avgsalary);
    }catch(IOException e){
        e.printStackTrace();
    }
    double sholiday;
    if(avgsalary>0) {
        sholiday = (Integer.parseInt(holiday.getText())) * avgsalary / norm;
    }else{
        sholiday=0;
    }*/
    let sholiday = holiday * swork / timenorm;

    let sovertime=overtime*2*oklad*proc/timenorm;

    let result=swork + ssick + sholiday + sovertime;

    document.getElementById("tax").value=result*0.13;
    document.getElementById("pension").value=result*0.01;
    document.getElementById("pensioninsurance").value=result*0.29;
    document.getElementById("socialinsurance").value=result*0.06;
    document.getElementById("salary").value=result;
    document.getElementById("netsalary").value=result*0.86;
}

<script src="../static/js/salaryaccount.js" th:src="@{/js/salaryaccount.js}"/>