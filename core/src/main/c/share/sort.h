//
// Created by blues on 20/02/2020.
//

#ifndef SORT_H
#define SORT_H

#include <jni.h>
#include "vcl/vectorclass.h"
#include "sort_vanilla.h"

#define POINTER_NAME(func) func ## _pointer
#define F_AVX512(func) func ## _AVX512
#define F_AVX2(func) func ## _AVX2
#define F_SSE41(func) func ## _SSE41
#define F_SSE2(func) func ## _SSE2
#define F_VANILLA(func) func ## _Vanilla
#define F_DISPATCH(func) func ## _dispatch

//void buildMaskArray(uint8_t mask, int64_t *array);

typedef int64_t LongLongVecFuncType(int64_t *, int64_t);

#define LONG_LONG_DISPATCHER(func) \
\
LongLongVecFuncType F_SSE2(func), F_SSE41(func), F_AVX2(func), F_AVX512(func), F_DISPATCH(func); \
\
LongLongVecFuncType *POINTER_NAME(func) = &func ## _dispatch; \
\
int64_t F_DISPATCH(func) (int64_t *pi, int64_t count) { \
    const int iset = instrset_detect();  \
    if (iset >= 10) { \
        POINTER_NAME(func) = &F_AVX512(func); \
    } else if (iset >= 8) { \
        POINTER_NAME(func) = &F_AVX2(func); \
    } else if (iset >= 5) { \
        POINTER_NAME(func) = &F_SSE41(func); \
    } else if (iset >= 2) { \
        POINTER_NAME(func) = &F_SSE2(func); \
    } else { \
        POINTER_NAME(func) = &F_VANILLA(func); \
    }\
    return (*POINTER_NAME(func))(pi, count); \
} \
\
inline int64_t func(int64_t *pl, int64_t count) { \
return (*POINTER_NAME(func))(pl, count); \
}\
\
extern "C" { \
JNIEXPORT jlong JNICALL Java_io_questdb_std_Range_ ## func(JNIEnv *env, jclass cl, jlong pLong, jlong count) { \
    return func((int64_t *) pLong, count); \
}\
\
}

#endif //SORT_H
