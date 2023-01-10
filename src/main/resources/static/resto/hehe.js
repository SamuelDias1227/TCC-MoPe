$(document).ready(function() {

	//pega o valor do id do filtro selecionado e verifica para ocutar o o nao selecionado
	$("#selectFiltro").change(function() {
		var idSelected = parseInt(this.value);
		switch(idSelected){
            case 1:
                $("#h1Permanencia").show();
			    $("#theadPermanenciasProfessor").show();
                $("#tboryPermanenciasProfessor").show();
                $("#h1Monitoria").show();
                $("#theadPermanenciasMonitor").show();
                $("#tboryPermanenciasMonitor").show();
            break;

            case 2:
                $("#h1Permanencia").show();
                $("#theadPermanenciasProfessor").show();
                $("#tboryPermanenciasProfessor").show();
                $("#h1Monitoria").hide();
                $("#theadPermanenciasMonitor").hide();
                $("#tboryPermanenciasMonitor").hide();
            break;

            case 3:
                $("#h1Permanencia").hide();
                $("#theadPermanenciasProfessor").hide();
                $("#tboryPermanenciasProfessor").hide();
                $("#h1Monitoria").show();
                $("#theadPermanenciasMonitor").show();
                $("#tboryPermanenciasMonitor").show();
            break;
        }
	});

    $("#inputPesquisa").keyup(function() {
        const input = this.value

        const professor = $(".nomeProfessor")

        const diaProfessor = $(".diaProfessor")

        const monitor = $(".nomeMonitor")

        const diaMonitor = $(".diaMonitor")

        for (let i = 0; i < professor.length; i++){
            if (professor[i].innerHTML.includes(input) || diaProfessor[i].innerHTML.includes(input)){
                //object[key][i].style.display = "none";
                $("#tabela-fileira").find("tr:gt("+i+")").show();
            }else{
                //object[key][i].style.display = "block";
                $("#tabela-fileira").find("tr:gt("+i+")").hide();
            }
        }

        for (let i = 0; i < monitor.length; i++){
            if (monitor[i].innerHTML.includes(input) || diaMonitor[i].innerHTML.includes(input)){
                //object[key][i].style.display = "none";
                $("#tabela-monitoria-fileira").find("tr:gt("+i+")").show();
            }else{
                //object[key][i].style.display = "block";
                $("#tabela-monitoria-fileira").find("tr:gt("+i+")").hide();
            }
        }


        /*for (let key in object){
            alert(key);
            for (let i = 0; i < object[key].length; i++){
                if (!object[key][i].innerHTML.includes(input)){
                    //object[key][i].style.display = "none";
                    $("#tabela-fileira").find("tr:gt("+i+")").hide();
                }else{
                    //object[key][i].style.display = "block";
                    $("#tabela-fileira").find("tr:gt("+i+")").show();
                }
            }
        }

        for (let key in objectMonitor){
            for (let i = 0; i < objectMonitor[key].length; i++){
                if (!objectMonitor[key][i].innerHTML.includes(input)){
                    //objectMonitor[key][i].style.display = "none";
                    $("#tabela-monitoria-fileira").find("tr:gt("+i+")").hide();
                }else{
                    //objectMonitor[key][i].style.display = "block";
                    $("#tabela-monitoria-fileira").find("tr:gt("+i+")").show();
                }
            }
        }*/
    });
    
});

function w3_open() {
    document.getElementById("main").style.marginLeft = "20%"
    document.getElementById("mySidebar").style.width = "20%"
    document.getElementById("footer").style.marginLeft = "15%"
    document.getElementById("mySidebar").style.display = "block"
    document.getElementById("openNav").style.display = 'none'
}

function w3_close() {
    document.getElementById("main").style.marginLeft = "0%"
    document.getElementById("footer").style.marginLeft = "0%"
    document.getElementById("mySidebar").style.display = "none"
    document.getElementById("openNav").style.display = "inline-block"
}

