<p></p><p></p><p></p><p><span style="font-size: 11pt;color: #000000;background-color: transparent;vertical-align: baseline;"></span></p><p><span style="vertical-align: baseline;">Pippin y Merry tienen mala fama de saquear los cultivos de algunos vecinos de la comarca y de las comarcas vecinas. Para ser &#34;equilibrados&#34; y no robarles siempre a los mismo llevan un registro de las frutas y verduras  según el mes del año.<br/></span><br/>Pipin y Morris tienen 16 vecinos, y cada uno tiene asignado una hectarea, dentro de un campo.<br/>Hay 4 campos, y cada campo tiene 4 hectareas.<br/><br/>La nota es asi, la función recibe un arreglo tridimensional de la forma:<br/>int cronograma_de_cosecha[MES_DEL_ANIO][N_CAMPOS][N_HECTAREAS]<br/><br/>Para un determinado mes del año, hay que recorrer <b><b><i>todos </i></b></b>las hectáreas de todos los campos, buscando una determinada fruta. <br/>Luego, si se encuentra una fruta, hay que verificar en el arreglo de vecinos, si el vecino con ese id de fruta ya fue visitado.<br/><br/>Por ejemplo:<br/><br/>cronograma_de_cosecha[2] ={ {-1,0,-1,-1},<br/>                                                       {-1,1,-1,-1},<br/>                                                       {-1,-1,-1,-1},<br/>                                                       {2,-1,-1,-1} }<br/><br/>En este ejemplo, podemos ver que para el mes 2, tenemos 4 campos con 4 hectareas cada uno.<br/>El primer campo seria:<br/>{-1,0,-1,-1}<br/><br/>Donde en las hectareas 0,2 y 3 no hay ninguna plantación. Pero en la hectarea 1 hay una fruta con id 0.<br/><br/>En el segundo campo:<br/>{-1,1,-1,-1}<br/><br/>Vemos que en la hectarea 1, hay una fruta con id 1.<br/><br/>En el tercer campo:<br/>{-1,-1,-1,-1}<br/><br/>No hay ninguna plantación de ningun tipo en ninguna hectarea.<br/><br/>En el ultimo campo:<br/>{2,-1,-1,-1}<br/><br/>Vemos que hay una fruta con id 2 en la hectarea 0.<br/><br/>Entonces, supongamos que yo estoy buscando la fruta 2, del mes 2 también. Voy a acceder a cronograma_de_cosecha[2] y voy a buscar en todos los campos y a ver si hay alguna hectarea en la que haya una fruta con id 2.<br/><br/>En nuestro caso, seria en el campo numero 3, en la hectarea numero 0. <br/><br/>Si encontramos la fruta en alguna hectarea de algun campo, tenemos que verificar en vecinos[2] (usando el id de la fruta) para ver si ya hemos visitado previamente a ese vecino.<span id="selectionBoundary_1590543476914_4932892895168821" class="rangySelectionBoundary">&#65279;</span></p><p></p>