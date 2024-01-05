/* Headers for the CIF to C translation of databased_supervisor.cif
 * Generated file, DO NOT EDIT
 */

#ifndef CIF_C_DATABASED_SUPERVISOR_ENGINE_H
#define CIF_C_DATABASED_SUPERVISOR_ENGINE_H

#include "databased_supervisor_library.h"

/* Types of the specification.
 * Note that integer ranges are ignored in C.
 */
/* CIF type: tuple(int var; int low; int high) */
struct T3III_struct {
    IntType _field0;
    IntType _field1;
    IntType _field2;
};
typedef struct T3III_struct T3IIIType;

extern BoolType T3IIITypeEquals(T3IIIType *left, T3IIIType *right);
extern int T3IIITypePrint(T3IIIType *tuple, char *dest, int start, int end);

/* CIF type: list[12] tuple(int var; int low; int high) */
struct A12T3III_struct {
    T3IIIType data[12];
};
typedef struct A12T3III_struct A12T3IIIType;

extern BoolType A12T3IIITypeEquals(A12T3IIIType *left, A12T3IIIType *right);
extern T3IIIType *A12T3IIITypeProject(A12T3IIIType *array, IntType index);
extern void A12T3IIITypeModify(A12T3IIIType *array, IntType index, T3IIIType *value);
extern int A12T3IIITypePrint(A12T3IIIType *array, char *dest, int start, int end);

enum Enumdatabased_supervisor_ {
    _databased_supervisor_Idle,
    _databased_supervisor_Off,
    _databased_supervisor_On,
    _databased_supervisor_Pushed,
    _databased_supervisor_Released,
    _databased_supervisor_Running,
    _databased_supervisor_StartTimer,
    _databased_supervisor_TurnLampOff,
    _databased_supervisor_TurnLampOn,
    _databased_supervisor_WaitForButtonPush,
    _databased_supervisor_WaitForTimeout,
};
typedef enum Enumdatabased_supervisor_ databased_supervisorEnum;

extern const char *enum_names[];
extern int EnumTypePrint(databased_supervisorEnum value, char *dest, int start, int end);

/* CIF type: list[6] bool */
struct A6B_struct {
    BoolType data[6];
};
typedef struct A6B_struct A6BType;

extern BoolType A6BTypeEquals(A6BType *left, A6BType *right);
extern BoolType A6BTypeProject(A6BType *array, IntType index);
extern void A6BTypeModify(A6BType *array, IntType index, BoolType value);
extern int A6BTypePrint(A6BType *array, char *dest, int start, int end);


/* Event declarations. */
enum databased_supervisorEventEnum_ {
    EVT_INITIAL_,       /**< Initial step. */
    EVT_DELAY_,         /**< Delay step. */
    EVT_TAU_,           /**< Tau step. */
    Button_u_pushed_,   /**< Event Button.u_pushed. */
    Button_u_released_, /**< Event Button.u_released. */
    Lamp_c_on_,         /**< Event Lamp.c_on. */
    Lamp_c_off_,        /**< Event Lamp.c_off. */
    Timer_c_start_,     /**< Event Timer.c_start. */
    Timer_u_timeout_,   /**< Event Timer.u_timeout. */
};
typedef enum databased_supervisorEventEnum_ databased_supervisor_Event_;

/** Names of all the events. */
extern const char *databased_supervisor_event_names[];

/* Constants. */
extern A12T3IIIType bdd_nodes_; /**< Constant "bdd_nodes". */

/* Input variables. */




/* Declaration of internal functions. */
extern BoolType bdd_eval_(IntType bdd_eval_idx_, A6BType* bdd_eval_values_tmp2);

/* State variables (use for output only). */
extern RealType model_time; /**< Current model time. */

/** Discrete variable "E Button". */
extern databased_supervisorEnum Button_;

/** Discrete variable "E Cycle". */
extern databased_supervisorEnum Cycle_;

/** Discrete variable "E Lamp". */
extern databased_supervisorEnum Lamp_;

/** Discrete variable "E Timer". */
extern databased_supervisorEnum Timer_;

/* Algebraic and derivative functions (use for output only). */

BoolType bdd_value0_(void);
BoolType bdd_value1_(void);
BoolType bdd_value2_(void);
BoolType bdd_value3_(void);
BoolType bdd_value4_(void);
BoolType bdd_value5_(void);
A6BType bdd_values_(void);


/* Code entry points. */
void databased_supervisor_EngineFirstStep(void);
void databased_supervisor_EngineTimeStep(double delta);

#if EVENT_OUTPUT
/**
 * External callback function reporting about the execution of an event.
 * @param event Event being executed.
 * @param pre If \c TRUE, event is about to be executed. If \c FALSE, event has been executed.
 * @note Function must be implemented externally.
 */
extern void databased_supervisor_InfoEvent(databased_supervisor_Event_ event, BoolType pre);
#endif

#if PRINT_OUTPUT
/**
 * External callback function to output the given text-line to the given filename.
 * @param text Text to print (does not have a EOL character).
 * @param fname Name of the file to print to.
 */
extern void databased_supervisor_PrintOutput(const char *text, const char *fname);
#endif

#endif

