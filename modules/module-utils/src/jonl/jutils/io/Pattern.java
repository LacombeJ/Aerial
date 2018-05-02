package jonl.jutils.io;

/*

Copyright (c) 2009, Nathan Sweet
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
      in the documentation and/or other materials provided with the distribution.
    * Neither the name of Esoteric Software nor the names of its contributors may be used to endorse or promote products derived from this
      software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

/**
 * https://github.com/EsotericSoftware/wildcard
 * 
 * Modified to expose Pattern class method : "matches()"
 * 
 */
public class Pattern {
    String value;
    boolean ignoreCase;
    final String[] values;

    public Pattern (String pattern, boolean ignoreCase) {
        this.ignoreCase = ignoreCase;

        pattern = pattern.replace('\\', '/');
        pattern = pattern.replaceAll("\\*\\*[^/]", "**/*");
        pattern = pattern.replaceAll("[^/]\\*\\*", "*/**");
        if (ignoreCase) pattern = pattern.toLowerCase();

        values = pattern.split("/");
        value = values[0];
    }

    public boolean matches (String fileName) {
        if (value.equals("**")) return true;

        if (ignoreCase) fileName = fileName.toLowerCase();

        // Shortcut if no wildcards.
        if (value.indexOf('*') == -1 && value.indexOf('?') == -1) return fileName.equals(value);

        int i = 0, j = 0;
        while (i < fileName.length() && j < value.length() && value.charAt(j) != '*') {
            if (value.charAt(j) != fileName.charAt(i) && value.charAt(j) != '?') return false;
            i++;
            j++;
        }

        // If reached end of pattern without finding a * wildcard, the match has to fail if not same length.
        if (j == value.length()) return fileName.length() == value.length();

        int cp = 0;
        int mp = 0;
        while (i < fileName.length()) {
            if (j < value.length() && value.charAt(j) == '*') {
                if (j++ >= value.length()) return true;
                mp = j;
                cp = i + 1;
            } else if (j < value.length() && (value.charAt(j) == fileName.charAt(i) || value.charAt(j) == '?')) {
                j++;
                i++;
            } else {
                j = mp;
                i = cp++;
            }
        }

        // Handle trailing asterisks.
        while (j < value.length() && value.charAt(j) == '*')
            j++;

        return j >= value.length();
    }
    
}
