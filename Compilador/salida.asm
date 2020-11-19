.386
.MODEL flat, stdcall
option casemap:none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
_f@main DW ? 
_c@main DW ? 
_z@main DW ? 
_d@main DW ? 
_a@main DW ? 
_4 DW 4
_3 DW 3
_e@main DW ? 
_1 DW 1
_b@main DW ? 

DivisionCero db "Error al intentar dividir por 0", 0
RestaNegativa db "Ocurrió overflow(resta negativa) en la resta", 0
estado DW ?
.code
start: 
Laa@main:
MOV CX,_a@main
CMP CX,_b@main
JNB L12
MOV BX,_a@main
MOV _4,BX
JMP L16
L12:
MOV BX,_a@main
MOV _1,BX
L16:
JMP Laa@main
invoke ExitProcess, 0
LabelDivisionCero:
invoke MessageBox, NULL, addr DivisionCero, addr DivisionCero, MB_OK
invoke ExitProcess, 0
LabelRestaNegativa:
invoke MessageBox, NULL, addr RestaNegativa, addr RestaNegativa, MB_OK
invoke ExitProcess, 0
end start 