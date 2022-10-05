/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chupitetris;

/**
 *
 * @author Samuel Novoa Comesaña <samuel.novoa97@gmail.com>
 */
public class PlayerScore {
    private String player;
    private int score;
    
    public PlayerScore() {
        this("local", 0);
    }
    
    public PlayerScore(String player) {
        this(player, 0);
    }
    
    public PlayerScore(String player, int score) {
        this.player = player;
        this.score = score;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
