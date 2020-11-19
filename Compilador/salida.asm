.386
.MODEL flat, stdcall
option casemap:none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
_c@ejemplo@main DW ? 
_b@ejemplo@main DW ? 
_5 DW 5
_4 DW 4
_3 DW 3
_a@ejemplo@main DW ? 
_2 DW 2
_1 DW 1

DivisionCero db "Error al intentar dividir por 0", 0
RestaNegativa db "Ocurrió overflow(resta negativa) en la resta", 0
CodigoTerminado db "Código finalizado correctamente", 0
estado DW ?
.code
start: 
Lejemplo@main:
MOV CX,_a@ejemplo@main
MOV _4,CX
MOV CX,_b@ejemplo@main
MOV _a@ejemplo@main,CX
MOV CX,_b@ejemplo@main
ADD CX,_2
MOV _c@ejemplo@main,CX
MOV CX,_c@ejemplo@main
CMP CX,_b@ejemplo@main
JNA L30
CMP _1,0
JE LabelDivisionCero
MOV AX,_5
IDIV _1
MOV _b@ejemplo@main,AX
MOV BX,_3
SUB BX,_4
JS LabelRestaNegativa
MOV _a@ejemplo@main,BX
JMP L31
L30:
L31:
JMP Lejemplo@main
invoke MessageBox, NULL, addr CodigoTerminado, addr CodigoTerminado, MB_OK
invoke ExitProcess, 0
LabelDivisionCero:
invoke MessageBox, NULL, addr DivisionCero, addr DivisionCero, MB_OK
invoke ExitProcess, 0
LabelRestaNegativa:
invoke MessageBox, NULL, addr RestaNegativa, addr RestaNegativa, MB_OK
invoke ExitProcess, 0
end start 