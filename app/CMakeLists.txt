cmake_minimum_required(VERSION 3.4.1)

#add_definitions(-std=c++11)
##设置生成的so动态库最后输出的路径
#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})

# 支持gnu++11
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

#lamemp3
#设置变量SRC_DIR为lamemp3的所在路径
#set(SRC_DIR src/main/cpp/lamemp3)
#指定头文件所在，可以多次调用，指定多个路径
include_directories(src/main/cpp/lamemp3)
#添加自自定义的so库时，有两种方式，一种添加一个目录，一种一个个文件添加
#设定一个目录
aux_source_directory(src/main/cpp/lamemp3 SRC_LIST)

#将前面目录下所有的文件都添加进去
add_library(lamemp3 SHARED src/main/cpp/SimpleLame.cpp ${SRC_LIST})

#一个个文件的加
#add_library(lame-mp3
#            SHARED
#            ${SRC_DIR}/bitstream.c
#            ${SRC_DIR}/encoder.c
#            ${SRC_DIR}/fft.c
#            ${SRC_DIR}/gain_analysis.c
#            ${SRC_DIR}/id3tag.c
#            ${SRC_DIR}/lame.c
#            ${SRC_DIR}/mpglib_interface.c
#            ${SRC_DIR}/newmdct.c
#            ${SRC_DIR}/presets.c
#            ${SRC_DIR}/psymodel.c
#            ${SRC_DIR}/quantize.c
#            ${SRC_DIR}/quantize_pvt.c
#            ${SRC_DIR}/reservoir.c
#            ${SRC_DIR}/set_get.c
#            ${SRC_DIR}/tables.c
#            ${SRC_DIR}/takehiro.c
#            ${SRC_DIR}/util.c
#            ${SRC_DIR}/vbrquantize.c
#            ${SRC_DIR}/VbrTag.c
#            ${SRC_DIR}/version.c
#            )



#ffmpeg
# 1. 定义so库和头文件所在目录，方面后面使用
set(ffmpeg_lib_dir ${CMAKE_SOURCE_DIR}/jniLibs/${ANDROID_ABI})
#set(ffmpeg_head_dir src/main/cpp/ffmpeg)
set(ffmpeg_head_dir ${CMAKE_SOURCE_DIR}/src/main/cpp/ffmpeg)
# 2. 添加头文件目录
include_directories(${ffmpeg_head_dir}/include)
# 3. 添加ffmpeg相关的so库


add_library( avutil
        SHARED
        IMPORTED )
set_target_properties( avutil
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavutil.so )

add_library( swresample
        SHARED
        IMPORTED )
set_target_properties( swresample
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libswresample.so )

add_library( avcodec
        SHARED
        IMPORTED )
set_target_properties( avcodec
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavcodec.so )

add_library( avfilter
        SHARED
        IMPORTED)
set_target_properties( avfilter
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavfilter.so )

add_library( swscale
        SHARED
        IMPORTED)
set_target_properties( swscale
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libswscale.so )

add_library( avformat
        SHARED
        IMPORTED)
set_target_properties( avformat
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavformat.so )

add_library( avdevice
        SHARED
        IMPORTED)
set_target_properties( avdevice
        PROPERTIES IMPORTED_LOCATION
        ${ffmpeg_lib_dir}/libavdevice.so )

#add_library( postproc
#        SHARED
#        IMPORTED )
#set_target_properties( postproc
#        PROPERTIES IMPORTED_LOCATION
#        ${ffmpeg_lib_dir}/libpostproc.so )

#jni-test
#add_library( # Sets the name of the library.
#             jni-test
#
#             # Sets the library as a shared library.
#             SHARED
#
#             # Provides a relative path to your source file(s).
#        src/main/cpp/aaa/test.cpp
#              )
#
#target_link_libraries( # Specifies the target library.
#        jni-test
#
#        # Links the target library to the log library
#        # included in the NDK.
#        ${log-lib} )

#native-lib
add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/nativetest/native-lib.cpp)




# 查找代码中使用到的系统库
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# 指定编译目标库时，cmake要链接的库
target_link_libraries(

        # 指定目标库，native-lib 是在上面 add_library 中配置的目标库
        native-lib

        # 4. 连接 FFmpeg 相关的库
        avutil
        swresample
        avcodec
        avfilter
        swscale
        avformat
        avdevice
#        postproc


        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} )


