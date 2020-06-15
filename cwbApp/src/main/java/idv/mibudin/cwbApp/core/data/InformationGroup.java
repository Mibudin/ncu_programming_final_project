package idv.mibudin.cwbApp.core.data;


import java.util.HashMap;
import java.util.Map;

import idv.mibudin.cwbApp.core.data.Information.InformationType;


public class InformationGroup
{
    private Map<InformationType, Information> informationsMap;


    public InformationGroup()
    {
        informationsMap = new HashMap<InformationType, Information>();
    }

    public void addInformation(Information information)
    {
        informationsMap.put(information.getType(), information);
    }

    public void addInformations(Information... informations)
    {
        for(Information information : informations)
        {
            addInformation(information);
        }
    }

    public Information getInformation(InformationType informationType)
    {
        return informationsMap.get(informationType);
    }

    public void clearInformations()
    {
        informationsMap.clear();
    }

    public Map<InformationType, Information> getInformations()
    {
        return informationsMap;
    }
}
