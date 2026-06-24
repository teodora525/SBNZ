# --- KONDICIJE (Uslovi) ---
[condition][]HTTP method is {method}=$req : RequestEvent(httpMethod == "{method}")
[condition][]body contains "{pattern}"=$req : RequestEvent(payload != null, payload.toLowerCase() contains "{pattern}")
[condition][]query contains "{keyword}"=$req : RequestEvent(queryParams != null, queryParams.toString().toUpperCase() contains "{keyword}")

# --- KONKLUZIJE (Akcije) ---
[consequence][]block request=modify($req) \{ setBlocked(true), setBlockReason("Blocked by DSL Rule"); \}
[consequence][]log as {threat_name}=insert(new ThreatAlert($req.getIpAddress(), "{threat_name}")); System.out.println("DSL ALARM: Detektovan " + "{threat_name}" + " sa IP: " + $req.getIpAddress());