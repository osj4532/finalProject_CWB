<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    
<!DOCTYPE html>
<html lang="ko">

<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="Dashboard">
  <meta name="keyword" content="Dashboard, Bootstrap, Admin, Template, Theme, Responsive, Fluid, Retina">
  <title>컴워밸 - Company&Worker balance</title>

  <link href="<c:url value='/resources/img/favicon.png'/>" rel="icon">
  <link href="<c:url value='/resources/img/apple-touch-icon.png'/>" rel="apple-touch-icon">

  <link href="<c:url value='/resources/lib/bootstrap/css/bootstrap.min1.css'/>"  rel="stylesheet">
  <link href="<c:url value='/resources/lib/font-awesome/css/font-awesome.css'/>"  rel="stylesheet" />
  <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/zabuto_calendar.css'/>" >
  <link rel="stylesheet" type="text/css" href="<c:url value='/resources/lib/gritter/css/jquery.gritter.css'/>"/>
  <!-- Custom styles for this template -->
   <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.2.0/css/all.css">
  
  <link href="<c:url value='/resources/css/style.css'/>" rel="stylesheet">
  <link href="<c:url value='/resources/css/style-responsive.css'/>"  rel="stylesheet">
  <script src="<c:url value='/resources/lib/chart-master/Chart.js'/>" ></script>
<link rel="stylesheet" type="text/css"
 href="<c:url value='/resources/lib/bootstrap-fileupload/bootstrap-fileupload.css'/>"/>

<script src="<c:url value='/resources/lib/jquery/jquery.min.js'/>"></script>	
<script type="text/javascript">
	$(function(){
		$('form[name=resourcesEdit]').submit(function(){
			$('input[type=text]').each(function(){
				if($(this).val()==''){
					if($(this).attr("name")=='resPrice'){
						$(this).val(0);  
					}else{   
						$('.chkInfo').show();
						$(this).focus();
						event.preventDefault();
					} 
				}
			});
			
			if($('#hidetext').is(':disabled')==false){
				$("#mapLatlng").val("!"+$('#hidetext').val());			
			}
		});
		
		<c:if test="${fn:indexOf(resVo.resLocation,'!')==0}">                 
			$("#map").hide();	
			$("#map").prev().hide();
	    </c:if>
	    
	    <c:if test="${fn:indexOf(resVo.resLocation,'!')!=0}">                 
			$("#hidetext").attr("disabled",true);
    	</c:if>
	   
		$('#findView').click(function(){
			$("#map").toggle(500);
			$("#map").prev().toggle(500);
			 if($("#hidetext").is(":disabled")){
				 $("#hidetext").removeAttr("disabled");
				 $("#hidetext").val("");
	        }
		});
		
	});

</script>
</head>
<body>
 <section id="container">
      <section class="wrapper" style="margin-top: 10px">
        <h3 style="margin-left: 15px"><i class="fas fa-edit"></i> 자원 수정 <b>${resVo.resName}</b></h3>
        <!-- BASIC FORM ELELEMNTS -->
        <div class="row">  
          <div class="col-lg-12"> 
            <div class="form-panel"> 
              <h4></h4>
              <form class="form-horizontal style-form" 
              enctype="multipart/form-data"
              method="post" 
              name="resourcesEdit"
              action="<c:url value='/resources/resourcesEdit.do'/>">
                <div class="form-group">
                  <label class="col-sm-2 col-sm-2 control-label">자원명</label>
                  <div class="col-sm-10">
                    <input type="hidden" class="form-control" name="resNo" value="${param.resNo}" >
                    <input type="text" class="form-control" name="resName" value="${resVo.resName}">
                  </div>	
                </div>
                <div class="form-group">
                  <label class="col-sm-2 col-sm-2 control-label">자원 가격</label>
                  <div class="col-sm-10">
                    <input type="text" class="form-control round-form" 
                    name="resPrice"
                    <c:if test="${resVo.resPrice!=0}">
                     value="${resVo.resPrice }"
                     </c:if>
                     >
                  </div> 
                </div>
                <div class="form-group">
                  <label class="col-sm-2 col-sm-2 control-label">자원 위치</label>
                  <div class="col-sm-10">
                    <input id='hidetext' type="text" class="form-control round-form" 
                    <c:if test="${fn:indexOf(resVo.resLocation,'!')!=0}">                 
                    value="외부 위치지정"
    				</c:if>
                    <c:if test="${fn:indexOf(resVo.resLocation,'!')==0}">                 
                  	  value="${fn:substringAfter(resVo.resLocation,'!')}" 
    				</c:if>
                    >
                    <input id='mapLatlng' type="hidden" name="resLocation">
                  <button type="button" class="badge btn-theme06" 
                  id="findView">외부 위치 지정</button>
                  </div>
                  <label class="col-sm-2 col-sm-2 control-label">자원 위치지도</label>
                  <div id="map" style="width:600px;height:400px;"></div>
                </div>
                <div class="form-group">
                  <label class="col-sm-2 col-sm-2 control-label">자원 정보</label>
                  <div class="col-sm-10">
                    <input type="text" class="form-control" 
                    name="resInformation" value="${resVo.resInformation }">
                  </div>
                </div> 

             <div class="form-group last">
                  <label class="control-label col-md-3">자원 이미지등록</label>
                  <input type="hidden" value="${resVo.resFilename }" name="oldImg"> 
                  <div class="col-md-9">
                    <div class="fileupload fileupload-new" data-provides="fileupload">
                      <div class="fileupload-new thumbnail" style="width: 200px; height: 150px;">
                        <img src="<c:url value='/resimg_file/${resVo.resFilename }'/>" alt="" />
                      </div>
                      <div class="fileupload-preview fileupload-exists thumbnail" style="max-width: 200px; max-height: 150px; line-height: 20px;"></div>
                      <div>
                        <span class="btn btn-theme02 btn-file">
                        <span class="fileupload-new"><i class="fa fa-undo"></i> 기존 사진 변경</span>
                        <span class="fileupload-exists"><i class="fa fa-undo"></i> 기존 사진 변경</span>
                        <input type="file" class="default" name="upimg"/>
                        
                        </span>
                        <br>
                        <br>
                        <button type="submit"
                        class="btn btn-theme">
                        <i class="fa fa-check"></i> 자원 변경</button>
                      </div>
                    </div>
                    <div class="chkInfo" style="display:none;">
                    <span class="label label-info">입력!</span>
                    <span>
                      정확하게 기입하여 주세요
                      </span>
                     </div> 
                  </div>
                </div>

              </form>
            </div>
          </div>
          <!-- col-lg-12-->
        </div>
    </section>
  <!--footer start-->
    <footer class="site-footer">
      <div class="text-center">
        <p>
          &copy; Copyrights <strong>Dashio</strong>. All Rights Reserved
        </p>
        <div class="credits">
          Created with Dashio template by <a href="https://templatemag.com/">TemplateMag</a>
        </div>
        <a href="index.html#" class="go-top">
          <i class="fa fa-angle-up"></i>
          </a>
      </div>
    </footer>
    <!--footer end  nav-expand.png-->
  </section>
  <!-- js placed at the end of the document so the pages load faster -->
	
  <script src="<c:url value='/resources/lib/bootstrap/js/bootstrap.min.js'/>"></script>
  <script class="include" type="text/javascript" src="<c:url value='/resources/lib/jquery.dcjqaccordion.2.7.js'/>"></script>
  <script src="<c:url value='/resources/lib/jquery.scrollTo.min.js'/>"></script>
  <script src="<c:url value='/resources/lib/jquery.nicescroll.js'/>" type="text/javascript"></script>
  <script src="<c:url value='/resources/lib/jquery.sparkline.js'/>"></script>
  
  <!--common script for all pages-->
  <script src="<c:url value='/resources/lib/common-scripts.js'/>"></script>
  <script type="text/javascript" src="<c:url value='/resources/lib/gritter/js/jquery.gritter.js'/>"></script>
  <script type="text/javascript" src="<c:url value='/resources/lib/gritter-conf.js'/>"></script>
  <!--script for this page--> 
  <script type="text/javascript" src="<c:url value='/resources/lib/bootstrap-fileupload/bootstrap-fileupload.js'/>"></script>
  <script src="<c:url value='/resources/lib/sparkline-chart.js'/>"></script>
  <script src="<c:url value='/resources/lib/zabuto_calendar.js'/>"></script>
    <script type="text/javascript" src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=66fc63c70e3d4b0aa612be53665e59ba&libraries=services"></script>
 <script>
			var container = document.getElementById('map');
			var options = {
				center: new kakao.maps.LatLng(
					<c:if test="${fn:indexOf(resVo.resLocation,'!')==0}">                 
    					37.50255739441079, 127.0247957449708
                    </c:if>
    				<c:if test="${fn:indexOf(resVo.resLocation,'!')!=0}"> 
	                    ${resVo.resLocation}
                    </c:if> 
				),
				level: 2
			}; 
			
			var hidetest = document.getElementById('hidetext'); 
		    var resultinput = document.getElementById('mapLatlng'); 
		    
		    
		    
			var map = new kakao.maps.Map(container, options);
			var geocoder = new kakao.maps.services.Geocoder();
			 var back = function(result, status) {
			        if (status === kakao.maps.services.Status.OK) {
			        	var address=result[0].address.address_name;
			        	hidetest.value="외부 위치지정("+result[0].address.address_name+")"; 
					    hidetest.disabled = true;
			        }          
			    };    
			var center = map.getCenter(); 
			
			<c:if test="${fn:indexOf(resVo.resLocation,'!')!=0}"> 
		    geocoder.coord2Address(center.getLng(), center.getLat(), back);
		    </c:if> 
			 
			// 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
			var zoomControl = new kakao.maps.ZoomControl();
			map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
			
			// 지도를 클릭한 위치에 표출할 마커입니다
			var marker = new kakao.maps.Marker({ 
			    // 지도 중심좌표에 마커를 생성합니다 
			    position: map.getCenter() 
			}); 
			// 지도에 마커를 표시합니다
			marker.setMap(map);
			
			
			
			// 지도에 클릭 이벤트를 등록합니다
			// 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
			kakao.maps.event.addListener(map, 'click', function(mouseEvent) {        
			    
			    // 클릭한 위도, 경도 정보를 가져옵니다 
			    var latlng = mouseEvent.latLng; 
			    
			    var callback = function(result, status) {
			        if (status === kakao.maps.services.Status.OK) {
			        	//address=result[0].address.address_name;
			        	hidetest.value="외부 위치지정("+result[0].address.address_name+")"; 
					    hidetest.disabled = true;
			        }          
			    };   
			    geocoder.coord2Address(latlng.getLng(), latlng.getLat(), callback);
			    
			    
			    // 마커 위치를 클릭한 위치로 옮깁니다
			    marker.setPosition(latlng);
			    
			    var message =  latlng.getLat()+','+latlng.getLng();
			    
			    
			    resultinput.value = message;
			    
			});  
			
			
</script>
</body>

</html>
