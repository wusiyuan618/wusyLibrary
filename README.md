# wusyLibrary
私人依赖库，囊括了再工作学习过程中的收录、自定义控件、工具类等等。  
对您如果有用，点个星谢谢。  
有什么问题，或者建议都希望Issues  
希望和大家一起学习和分享  
个人博客简书地址：https://www.jianshu.com/ 简书上的链接的所有工具类和自定义View都能够在wusyLibrary中找到。  

### 利用gradle引入项目中
setp 1:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
setp 2:
```
dependencies {
	        implementation 'com.github.wusiyuan618:wusyLibrary:1.2.0'
	}
```
### 常见错误：
`Error: Invoke-customs are only supported starting with Android O`
解决方式--->在gradle.build中添加以下内容：
```
android{
	......
	compileOptions {
		sourceCompatibility JavaVersion.VERSION_1_8
    		targetCompatibility JavaVersion.VERSION_1_8
	}
	......
}
```
