.386
.MODEL flat, stdcall
option casemap:none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
_5 DW 5
_4 DW 4
_b@main DW ? 
_a@main DW ? 
_2 DW 2

DivisionCero db "Error al intentar dividir por 0", 0
RestaNegativa db "Ocurrió overflow(resta negativa) en la resta", 0
CodigoTerminado db "Código finalizado correctamente", 0
estado DW ?
.code
start: 
MOV CX,_a@main
CMP CX,_2
JNB L11
MOV BX,_b@main
MOV _4,BX
JMP L15
L11:
MOV BX,_b@main
MOV _5,BX
L15:
invoke MessageBox, NULL, addr CodigoTerminado, addr CodigoTerminado, MB_OK
invoke ExitProcess, 0
LabelDivisionCero:
invoke MessageBox, NULL, addr DivisionCero, addr DivisionCero, MB_OK
invoke ExitProcess, 0
LabelRestaNegativa:
invoke MessageBox, NULL, addr RestaNegativa, addr RestaNegativa, MB_OK
invoke ExitProcess, 0
end start 