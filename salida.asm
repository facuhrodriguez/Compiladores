.386
.MODEL flat, stdcall
option casemap:none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
Todosaliomal DB "Todosaliomal", 0
_4 DW 4
TodoSaliobien DB "TodoSaliobien", 0
_2 DW 2
_1 DW 1
_c@aa@main DW ? 

DivisionCero db "Error al intentar dividir por 0", 0
RestaNegativa db "Ocurrió overflow(resta negativa) en la resta", 0
CodigoTerminado db "Código finalizado correctamente", 0
estado DW ?
.code
start: 
Laa@main:
MOV CX,_1
ADD CX,_4
MOV _c@aa@main,CX
MOV CX,_c@aa@main
CMP CX,_2
JNA L16
invoke MessageBox, NULL, addr TodoSaliobien, addr TodoSaliobien,MB_OK
JMP L19
L16:
invoke MessageBox, NULL, addr Todosaliomal, addr Todosaliomal,MB_OK
L19:
JMP Laa@main
invoke MessageBox, NULL, addr CodigoTerminado, addr CodigoTerminado, MB_OK
invoke ExitProcess, 0
LabelDivisionCero:
invoke MessageBox, NULL, addr DivisionCero, addr DivisionCero, MB_OK
invoke ExitProcess, 0
LabelRestaNegativa:
invoke MessageBox, NULL, addr RestaNegativa, addr RestaNegativa, MB_OK
invoke ExitProcess, 0
end start 