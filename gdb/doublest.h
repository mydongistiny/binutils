/* Floating point definitions for GDB.

   Copyright (C) 1986-2017 Free Software Foundation, Inc.

   This file is part of GDB.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

#ifndef DOUBLEST_H
#define DOUBLEST_H

struct type;
struct floatformat;

/* Use `long double' if the host compiler supports it.  (Note that this is not
   necessarily any longer than `double'.  On SunOS/gcc, it's the same as
   double.)  This is necessary because GDB internally converts all floating
   point values to the widest type supported by the host.

   There are problems however, when the target `long double' is longer than the
   host's `long double'.  In general, we'll probably reduce the precision of
   any such values and print a warning.  */

#if (defined HAVE_LONG_DOUBLE && defined PRINTF_HAS_LONG_DOUBLE \
     && defined SCANF_HAS_LONG_DOUBLE)
typedef long double DOUBLEST;
# define DOUBLEST_PRINT_FORMAT "Lg"
# define DOUBLEST_SCAN_FORMAT "Lg"
#else
typedef double DOUBLEST;
# define DOUBLEST_PRINT_FORMAT "g"
# define DOUBLEST_SCAN_FORMAT "lg"
/* If we can't scan or print long double, we don't want to use it
   anywhere.  */
# undef HAVE_LONG_DOUBLE
# undef PRINTF_HAS_LONG_DOUBLE
# undef SCANF_HAS_LONG_DOUBLE
#endif

/* Different kinds of floatformat numbers recognized by
   floatformat_classify.  To avoid portability issues, we use local
   values instead of the C99 macros (FP_NAN et cetera).  */
enum float_kind {
  float_nan,
  float_infinite,
  float_zero,
  float_normal,
  float_subnormal
};

extern void floatformat_to_doublest (const struct floatformat *,
				     const void *in, DOUBLEST *out);
extern void floatformat_from_doublest (const struct floatformat *,
				       const DOUBLEST *in, void *out);

extern int floatformat_is_negative (const struct floatformat *,
				    const bfd_byte *);
extern enum float_kind floatformat_classify (const struct floatformat *,
					     const bfd_byte *);
extern const char *floatformat_mantissa (const struct floatformat *,
					 const bfd_byte *);

extern std::string floatformat_to_string (const struct floatformat *fmt,
					  const gdb_byte *in);

/* Return the floatformat's total size in host bytes.  */

extern size_t floatformat_totalsize_bytes (const struct floatformat *fmt);

extern DOUBLEST extract_typed_floating (const void *addr,
					const struct type *type);
extern void store_typed_floating (void *addr, const struct type *type,
				  DOUBLEST val);
extern void convert_typed_floating (const void *from,
				    const struct type *from_type,
                                    void *to, const struct type *to_type);

#endif
