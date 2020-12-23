.386
.MODEL flat, stdcall
option casemap:none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
TodoAndabien DB "TodoAndabien", 0
_m@main DW ? 
_c@a@main DW ? 
_b@a@main DW ? 
_3 DW 3
_2 DW 2
Algosaliomal DB "Algosaliomal", 0

DivisionCero db "Error al intentar dividir por 0", 0
RestaNegativa db "Ocurrió overflow(resta negativa) en la resta", 0
CodigoTerminado db "Código finalizado correctamente", 0
estado DW ?
.code
start: 
La@main:
MOV CX,_b@a@main
ADD CX,_2
MOV _c@a@main,CX
MOV CX,_3
SUB CX,_3
JS LabelRestaNegativa
MOV BX,_3
CMP BX,_2
JNA L21
invoke MessageBox, NULL, addr TodoAndabien, addr TodoAndabien,MB_OK
JMP L24
L21:
invoke MessageBox, NULL, addr Algosaliomal, addr Algosaliomal,MB_OK
L24:
JMP L29
JMP La@main
L29:
invoke MessageBox, NULL, addr CodigoTerminado, addr CodigoTerminado, MB_OK
invoke ExitProcess, 0
LabelDivisionCero:
invoke MessageBox, NULL, addr DivisionCero, addr DivisionCero, MB_OK
invoke ExitProcess, 0
LabelRestaNegativa:
invoke MessageBox, NULL, addr RestaNegativa, addr RestaNegativa, MB_OK
invoke ExitProcess, 0
end start 