
#### 第三章 编译着色器及在屏幕上绘图

第二章的内容也在这里面一起使用。


#### 小结一下
1.创建着色器(shader)，分为顶点着色器和片段着色器，编译着色器，拿到着色器id

2.编译着色器，拿到着色器id

3.创建程序对象，返回id

4.附上着色器，两个都附加到程序对象上去。
```
    glAttachShader(programObjectId, vertexShaderId);
    glAttachShader(programObjectId, fragmentShaderId);
``` 

5.链接程序  glLinkProgram(programObjectId);

6.使用程序  glUseProgram(program);

7.程序链接成功后，查uniform的位置
```
    uColorLocation = glGetUniformLocation(program, U_COLOR);
```

8.把顶点数据设置进去
```
          // 表示从开着开始读
          vertexData.position(0);
          // 这一步是告诉OpenGL到哪里找到属性a_Position对应的数据
          glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,
                  false, 0, vertexData);
  
          glEnableVertexAttribArray(aPositionLocation);
```

9.利用顶点数据和设置片段着色器颜色进行绘制
```
        // 清空屏幕上的所有颜色，并使用之前设置的glClearColor填充。
        glClear(GL_COLOR_BUFFER_BIT);

        // 画三角形
        // 更新着色器代码中的u_Color的值
        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        // OpenGL只能画 点、线、三角形，这里第一个参数是告诉它我要画的是三角形
        // 然后从0位置开始读，读入6个顶点，这里就是对应了两个三角形
        glDrawArrays(GL_TRIANGLES, 0, 6);

        // 画线条
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        // 画两个点
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
```