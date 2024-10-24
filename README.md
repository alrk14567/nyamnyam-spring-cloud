# nyamnyam-kr
### 깃 설정
 <br>

**깃 최초설정**  
```git config --global user.name "(본인이름)" ```
``` git config --global user.email "(본인 이메일)" ```


**설정 확인**
```git config --global user.name/user.email```

**기본 브랜치명 변경**
```git config --global init.defaultBranch main```


<br>

---------------------------------------
<br>


### 깃 기본 명령어
<br>

**원격의 커밋 당겨오기**
``` git pull```

**변경사항 확인**
```git status```

**변경한 파일 담기**  
```git add(파일명)```

**변경할 파일 전부 담기**
```git add .```

**메시지와 함께 커밋**
```git commit -m "(커밋메시지)"```


**소스트리로 확인**
``` git log / git diff // 위아래로 스크롤: K,J```

**원격으로 push**
``` git push```

<br>

---------------------------------------
<br>

### pull 할 것이 있을 때 실수로 push를 하면?
<br>

#### 1. merge 방법
```git pull --no-rebase```
#### 2. rebase 방법
```git pull --rebase // pull할때의 rebase는 협업에서도ok ```

#### 3. 1 or 2 번 이후에 push 하기
#### 4. 3번 이후 다시 pull 하여 충동해결

<br>

------------------------------
<br>

### 과거로 돌아가기
<br>


**- reset를 사용하기**
1 git log 후 되돌아갈 커밋 해시 복사
2. git reset --hard (돌아갈 커밋 해쉬)
(뒤에 커밋해쉬가 없다면 마지막 커밋을 가리킴)

``` git reset --hard //마지막 커밋으로 돌아가기```

  	3. 이후 강제 푸시하기
    ``` git push -f origin <branch-name>```
    <br>


**- revert를 사용하기**
1. ```git revert(되돌릴 커밋해시)```
2. ``` wq!```로 커밋메시지 저장
<br>

**- 커밋하지 않고 revert하기**
``` git revert --no commit(돌아갈 커밋해시)```

<br>

_____________________
<br>

### branch 생성 / 이동 / 삭제
<br>

**생성 :**  ```git branch (branch-name) ```

**목록확인 :** ```  git branch ```

**이동 :**```git switch (이동할 브랜치명)```

**생성과 동시에 이동 :** ```git switch -c  (새 브랜치명)```

**삭제 :** ```git branch -d  (삭제할 브랜치명)```

**강제삭제 :** ```git branch -D (삭제할 브랜치명)```

**이름 변경 :** ```git branch -m (기존 브랜치명) (새 브랜치명)```

**여러브랜치 내역 보기 :**
```git log --all --decorate --oneline --graph```

<br> 

-----------------------
<br>

### branch 합치기
<br>

> merge : 두개의 브랜치를 한 커밋에 이어붙인다.
>% merge는 하나의 커밋이기 때문에 reset으로 되돌리기 가능)

> rebase : 브랜치를 다른 브랜치에 이어붙인다.
<br>

**yujeong 브랜치를 main으로 병합한다고 가정**

#### merge 로 병합
1. 만약 yujeong브랜치를 main으로 병합한다면 main으로 이동.

2. main에서 병합하기 : ``` git merge yujeong```
   이후 "wq"로 자동입력된 커밋메시지를 저장해 마무리

rebase 로 병합
1.  merge와 반대로 브랜치 이동을 먼저 해준다. (yujeong 으로 이동)
    : ``` git switch yujeong```


2. 그리고 ```git rebase main```
   ( rebase후 main은 뒤에있는 상황 )

3. main으로 이동후 fast-forward
   : 1. ```git switch main```(메인으로) 2. ``` git merge yujeong```(fast-forward)


4. yujeong 브랜치 삭제

<br>

----------------------
<br>

### 충돌 해결하기
<br>

merge로 해결하기
1. 당장 해결이 어려울 경우 merge를 통해 병합중단
   : ``` git merge --abort```

2. 코드를 수정한 후 ```git add .``` , ```git commit```으로 병합완료

3. 명령어로 계속하기 : ``` git rebase continue```

4. 위 과정을 충돌이 해결될 때까지 반복

<br>

--------------
<br>

### 원격 저장소 추가 후 푸시
<br>

1. 레포지 생성 후 명령어
: ``` git remote add origin (원격 저장소 주소) ```

2. 기본 브랜치명을 main으로 ( 깃허브 권장 )
: ``` git branch -M main```

 3. 로컬 저장소의 커밋 내역들을 원격으로 push하기
: ``` git push -u origin main```
> -u / --set-upStream : 현재 브랜치와 명시된 원격 브랜치 연결

<br>

- **원격 목록 보기 :** ``` git remote```

- **로컬과의 원격 없애기 (깃허브 레포지는 그대로)**
	**:** ``` git remote remove (origin 등 원격이름) ```

- **깃허브에서 클론하기 :**``` git clone (원격주소)```

<br>

-----------------------------
<br>

### 로컬에서 브랜치 만들어 원격에 push 하기
<br>

#### 1. 로컬에서 브랜치 생성
#### 2. git push 하면 대상을 명시하라는 메시지가 뜸
#### 3. 아래의 명령어로 원격의 브랜치 명시 및 기본설정
**:**``` git push -u origin (로컬의 브랜치명) ```

#### 로컬과 원격의 브랜치 목록 확인
**:** ```git branch --all```

<br>

--------------------
<br>

###


tracking information이 없을 때
: ``` git pull origin master // git pull origin yujeong```
> - origin(원격이름) , yujeong(연결할 로컬 브랜치 이름)

#### 로컬 브랜치의 원격추격 확인 :  ``` git branch -vv```

#### 이후 다시: git pull

그래도 안된다면
: ``` git branch --set-upstream-to=origin/<branch> <브랜치명>```
이후 다시 : git pull




이거 써야 커밋되고 머지가 되드라


 





# nyamnyamMSA
# nyamnyamMSA
