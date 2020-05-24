/*
 * Copyright (C) Discoveri SIA - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */
package eu.discoveri.lemmas.db;

import eu.discoveri.lemmas.LangCode;
import eu.discoveri.lemmas.PennPOSCode;


/**
 *
 * @author Chris Powell, Discoveri OU
 * @email info@astrology.ninja
 */
public class DictType
{
    private final String        fName;
    private final PennPOSCode   pCode;
    private final LangCode      lang;
    
    public DictType( String fName, PennPOSCode pCode, LangCode lang )
    {
        this.fName = fName;
        this.pCode = pCode;
        this.lang = lang;
    }

    public String getfName() { return fName; }
    public PennPOSCode getPOSCode() { return pCode; }
    public LangCode getLangCode() { return lang; }
}
