cat << EOF
OUTPUT_FORMAT("elf32-v850", "elf32-v850",
	      "elf32-v850")
OUTPUT_ARCH(v850)
ENTRY(_start)
SEARCH_DIR(.);
/*/critters/slug/grossman/install/sun4/v850-elf/lib*/
SECTIONS
{
  /* This saves a little space in the ELF file, since the zda starts
     at a higher location that the ELF headers take up.  */

  .zdata ${ZDATA_START_ADDR} : {
	*(.zdata)
	*(.zbss)
	*(reszdata)
	*(.zcommon)
  }

  /* This is the read only part of the zero data area.
     Having it as a seperate section prevents its
     attributes from being inherited by the zdata
     section.  Specifically it prevents the zdata
     section from being marked READONLY.  */

  .rozdata ${ROZDATA_START_ADDR} : {
	*(.rozdata)
	*(romzdata)
	*(romzbss)
  }

  /* Read-only sections, merged into text segment: */
  . = ${TEXT_START_ADDR};
  .interp	: { *(.interp) }
  .hash		: { *(.hash) }
  .dynsym	: { *(.dynsym) }
  .dynstr	: { *(.dynstr) }
  .rel.text	: { *(.rel.text) }
  .rela.text	: { *(.rela.text) }
  .rel.data	: { *(.rel.data) }
  .rela.data	: { *(.rela.data) }
  .rel.rodata	: { *(.rel.rodata) }
  .rela.rodata	: { *(.rela.rodata) }
  .rel.got	: { *(.rel.got) }
  .rela.got	: { *(.rela.got) }
  .rel.ctors	: { *(.rel.ctors) }
  .rela.ctors	: { *(.rela.ctors) }
  .rel.dtors	: { *(.rel.dtors) }
  .rela.dtors	: { *(.rela.dtors) }
  .rel.init	: { *(.rel.init) }
  .rela.init	: { *(.rela.init) }
  .rel.fini	: { *(.rel.fini) }
  .rela.fini	: { *(.rela.fini) }
  .rel.bss	: { *(.rel.bss) }
  .rela.bss	: { *(.rela.bss) }
  .rel.plt	: { *(.rel.plt) }
  .rela.plt	: { *(.rela.plt) }
  .init		: { KEEP (*(.init)) } =0
  .plt		: { *(.plt) }

  .text		: {
    *(.text)
    ${RELOCATING+*(.text.*)}
    /* .gnu.warning sections are handled specially by elf32.em.  */
    *(.gnu.warning)
    *(.gnu.linkonce.t*)
  } =0

  ${RELOCATING+_etext = .;}
  ${RELOCATING+PROVIDE (etext = .);}

/* start-sanitize-v850e */
   /* This is special code area at the end of the normal text section.
      It contains a small lookup table at the start followed by the
      code pointed to by entries in the lookup table.  */

  .call_table_data ${CALL_TABLE_START_ADDR} : {
    ${RELOCATING+PROVIDE(__ctbp = .);}
    *(.call_table_data)
  } = 0xff   /* fill gaps with 0xff */
  .call_table_text : {
    *(.call_table_text)
  }

/* end-sanitize-v850e */

  .fini		: { KEEP (*(.fini))    } =0
  .rodata	: { *(.rodata) ${RELOCATING+*(.rodata.*)} *(.gnu.linkonce.r*) }
  .rodata1	: { *(.rodata1) }

  .data		: {
    *(.data)
    ${RELOCATING+*(.data.*)}
    *(.gnu.linkonce.d*)
    CONSTRUCTORS
  }
  .data1	: { *(.data1) }
  .ctors	: {
    ${RELOCATING+___ctors = .;}
    KEEP (*(.ctors))
    ${RELOCATING+___ctors_end = .;}
  }

  .dtors	: {
    ${RELOCATING+___dtors = .;}
    KEEP (*(.dtors))
    ${RELOCATING+___dtors_end = .;}
  }

  .got		: { *(.got.plt) *(.got) }
  .dynamic	: { *(.dynamic) }

  .tdata ${TDATA_START_ADDR} : {
	${RELOCATING+PROVIDE (__ep = .);}
	*(.tbyte)
	*(.tcommon_byte)
	*(.tdata)
	*(.tbss)
	*(.tcommon)
  }

  /* We want the small data sections together, so single-instruction offsets
     can access them all, and initialized data all before uninitialized, so
     we can shorten the on-disk segment size.  */
  .sdata ${SDATA_START_ADDR} : {
	${RELOCATING+PROVIDE (__gp = . + 0x8000);}
	*(.sdata)
	${RELOCATING+__sbss_start = .;}
	*(.sbss)
	*(.scommon)
  }

  /* See comment about .rozdata  */
  .rosdata ${ROSDATA_START_ADDR} : {
	*(.rosdata)
  }

  ${RELOCATING+_edata  = DEFINED (__sbss_start) ? __sbss_start : . ;}
  ${RELOCATING+PROVIDE (edata = _edata);}

  .bss       :
  {
	${RELOCATING+__bss_start = DEFINED (__sbss_start) ? __sbss_start : . ;}
	*(.dynbss)
	*(.bss)
	*(COMMON)
  }

  ${RELOCATING+_end = . ;}
  ${RELOCATING+PROVIDE (end = .);}

  /* Stabs debugging sections.  */
  .stab 0		: { *(.stab) }
  .stabstr 0		: { *(.stabstr) }
  .stab.excl 0		: { *(.stab.excl) }
  .stab.exclstr 0	: { *(.stab.exclstr) }
  .stab.index 0		: { *(.stab.index) }
  .stab.indexstr 0	: { *(.stab.indexstr) }
  .comment 0		: { *(.comment) }

  /* DWARF debug sections.
     Symbols in the DWARF debugging sections are relative to the beginning
     of the section so we begin them at 0.  */

  /* DWARF 1 */
  .debug          0	: { *(.debug) }
  .line           0	: { *(.line) }

  /* GNU DWARF 1 extensions */
  .debug_srcinfo  0	: { *(.debug_srcinfo) }
  .debug_sfnames  0	: { *(.debug_sfnames) }

  /* DWARF 1.1 and DWARF 2 */
  .debug_aranges  0	: { *(.debug_aranges) }
  .debug_pubnames 0	: { *(.debug_pubnames) }

  /* DWARF 2 */
  .debug_info     0	: { *(.debug_info) }
  .debug_abbrev   0	: { *(.debug_abbrev) }
  .debug_line     0	: { *(.debug_line) }
  .debug_frame    0	: { *(.debug_frame) }
  .debug_str      0	: { *(.debug_str) }
  .debug_loc      0	: { *(.debug_loc) }
  .debug_macinfo  0	: { *(.debug_macinfo) }

  /* SGI/MIPS DWARF 2 extensions */
  .debug_weaknames 0	: { *(.debug_weaknames) }
  .debug_funcnames 0	: { *(.debug_funcnames) }
  .debug_typenames 0	: { *(.debug_typenames) }
  .debug_varnames  0	: { *(.debug_varnames) }

  /* User stack */
  .stack 0x200000	: {
	${RELOCATING+__stack = .;}
	*(.stack)
  }
  /* These must appear regardless of  .  */
}
EOF
