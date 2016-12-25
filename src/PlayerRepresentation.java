
public enum PlayerRepresentation
{
    LEFT, RIGHT;

    public static PlayerRepresentation getOpposite(PlayerRepresentation p)
    {
        return p == LEFT ? RIGHT : LEFT;
    }
}
