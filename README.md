

<img src="https://raw.githubusercontent.com/jangyoun/android-recyclerview-with-header/master/preivew.gif" width="250">

# RecyclerViewWithHeader

<img src="https://raw.githubusercontent.com/jangyoun/android-recyclerview-with-header/master/description.png">

##Description
- Using [android-parallax-recyclerview lib](https://github.com/kanytu/android-parallax-recyclerview)
- Using [HTTP JSON DATA](http://leejangyoun.com/android/dummy/recyclerViewWithHeader_1.json)

 - HTTP 통신
   - JSON Array 갯수 만큼 List에 추가함
   - LIST TYPE은 3가지
      - ITEM
      - GROUP : 내부 ITEM은 동적으로 할당
      - PROGRESS
   - page 적용 : JSON DATA의 last 인자를 통해, 마지막 페이지를 인지함
   - 리스트의 마지막에 도달하면, HTTP reload 함

 - 기타 
   - http 통신의 경우, volley stringrequest 사용함
   - glide image loader 사용함

